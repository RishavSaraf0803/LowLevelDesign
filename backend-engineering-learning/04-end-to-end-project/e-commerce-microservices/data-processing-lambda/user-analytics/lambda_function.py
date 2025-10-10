"""
User Analytics Lambda Function

This Lambda function processes user behavior data and generates analytics insights.
It demonstrates real-time data processing patterns commonly used in e-commerce platforms.

Architecture Decisions:
- Uses AWS Lambda for serverless, cost-effective processing
- Integrates with S3 for data storage and DynamoDB for results
- Implements batch processing for efficiency
- Provides error handling and monitoring

Use Cases:
- User behavior analysis
- Recommendation engine data preparation
- Fraud detection feature engineering
- Marketing campaign effectiveness analysis
"""

import json
import boto3
import pandas as pd
import numpy as np
from datetime import datetime, timedelta
from typing import Dict, List, Any
import logging
import os

# Configure logging
logger = logging.getLogger()
logger.setLevel(logging.INFO)

# AWS clients
s3_client = boto3.client('s3')
dynamodb = boto3.resource('dynamodb')
sns_client = boto3.client('sns')

# Environment variables
ANALYTICS_BUCKET = os.environ.get('ANALYTICS_BUCKET', 'ecommerce-analytics')
RESULTS_TABLE = os.environ.get('RESULTS_TABLE', 'user-analytics-results')
SNS_TOPIC_ARN = os.environ.get('SNS_TOPIC_ARN')

class UserAnalyticsProcessor:
    """
    Processes user analytics data and generates insights
    """
    
    def __init__(self):
        self.results_table = dynamodb.Table(RESULTS_TABLE)
    
    def process_user_registration_trends(self, data: pd.DataFrame) -> Dict[str, Any]:
        """
        Analyze user registration trends
        
        Args:
            data: DataFrame with user registration data
            
        Returns:
            Dictionary with trend analysis results
        """
        logger.info("Processing user registration trends")
        
        # Convert timestamp to datetime
        data['registration_date'] = pd.to_datetime(data['created_at'])
        data['date'] = data['registration_date'].dt.date
        
        # Daily registration counts
        daily_registrations = data.groupby('date').size().reset_index(name='count')
        
        # Weekly trend analysis
        data['week'] = data['registration_date'].dt.isocalendar().week
        data['year'] = data['registration_date'].dt.year
        weekly_registrations = data.groupby(['year', 'week']).size().reset_index(name='count')
        
        # Growth rate calculation
        daily_registrations = daily_registrations.sort_values('date')
        daily_registrations['growth_rate'] = daily_registrations['count'].pct_change() * 100
        
        # User source analysis
        if 'registration_source' in data.columns:
            source_analysis = data['registration_source'].value_counts().to_dict()
        else:
            source_analysis = {}
        
        # Demographics analysis
        demographics = {}
        if 'date_of_birth' in data.columns:
            data['age'] = (datetime.now() - pd.to_datetime(data['date_of_birth'])).dt.days / 365.25
            demographics['age_distribution'] = {
                'under_25': len(data[data['age'] < 25]),
                '25_35': len(data[(data['age'] >= 25) & (data['age'] < 35)]),
                '35_45': len(data[(data['age'] >= 35) & (data['age'] < 45)]),
                'over_45': len(data[data['age'] >= 45])
            }
        
        results = {
            'analysis_type': 'registration_trends',
            'total_users': len(data),
            'date_range': {
                'start': str(data['date'].min()),
                'end': str(data['date'].max())
            },
            'daily_average': daily_registrations['count'].mean(),
            'peak_day': {
                'date': str(daily_registrations.loc[daily_registrations['count'].idxmax(), 'date']),
                'count': int(daily_registrations['count'].max())
            },
            'growth_rate_avg': daily_registrations['growth_rate'].mean(),
            'source_analysis': source_analysis,
            'demographics': demographics,
            'generated_at': datetime.utcnow().isoformat()
        }
        
        logger.info(f"Registration trends analysis completed: {results['total_users']} users analyzed")
        return results
    
    def process_user_engagement_metrics(self, data: pd.DataFrame) -> Dict[str, Any]:
        """
        Calculate user engagement metrics
        
        Args:
            data: DataFrame with user activity data
            
        Returns:
            Dictionary with engagement metrics
        """
        logger.info("Processing user engagement metrics")
        
        # Convert timestamps
        data['login_date'] = pd.to_datetime(data['last_login'])
        data['created_date'] = pd.to_datetime(data['created_at'])
        
        # Calculate days since registration
        data['days_since_registration'] = (datetime.now() - data['created_date']).dt.days
        
        # Calculate days since last login
        data['days_since_last_login'] = (datetime.now() - data['login_date']).dt.days
        
        # Engagement categories
        active_users = len(data[data['days_since_last_login'] <= 7])  # Active in last 7 days
        inactive_users = len(data[data['days_since_last_login'] > 30])  # Inactive for 30+ days
        at_risk_users = len(data[(data['days_since_last_login'] > 7) & 
                                (data['days_since_last_login'] <= 30)])  # At risk users
        
        # User lifecycle analysis
        new_users = len(data[data['days_since_registration'] <= 30])  # Registered in last 30 days
        established_users = len(data[data['days_since_registration'] > 90])  # Registered 90+ days ago
        
        # Email verification impact
        if 'email_verified' in data.columns:
            verified_active_rate = len(data[(data['email_verified'] == True) & 
                                          (data['days_since_last_login'] <= 7)]) / len(data[data['email_verified'] == True]) * 100
            unverified_active_rate = len(data[(data['email_verified'] == False) & 
                                            (data['days_since_last_login'] <= 7)]) / len(data[data['email_verified'] == False]) * 100
        else:
            verified_active_rate = 0
            unverified_active_rate = 0
        
        results = {
            'analysis_type': 'engagement_metrics',
            'total_users': len(data),
            'engagement_categories': {
                'active_users': active_users,
                'at_risk_users': at_risk_users,
                'inactive_users': inactive_users,
                'active_rate': (active_users / len(data)) * 100,
                'churn_risk_rate': (at_risk_users / len(data)) * 100,
                'inactive_rate': (inactive_users / len(data)) * 100
            },
            'user_lifecycle': {
                'new_users': new_users,
                'established_users': established_users,
                'retention_rate': (established_users / len(data)) * 100
            },
            'verification_impact': {
                'verified_active_rate': verified_active_rate,
                'unverified_active_rate': unverified_active_rate,
                'verification_boost': verified_active_rate - unverified_active_rate
            },
            'average_days_since_last_login': data['days_since_last_login'].mean(),
            'generated_at': datetime.utcnow().isoformat()
        }
        
        logger.info(f"Engagement metrics completed: {active_users} active users out of {len(data)}")
        return results
    
    def detect_anomalies(self, data: pd.DataFrame) -> Dict[str, Any]:
        """
        Detect anomalies in user behavior patterns
        
        Args:
            data: DataFrame with user data
            
        Returns:
            Dictionary with anomaly detection results
        """
        logger.info("Running anomaly detection")
        
        anomalies = []
        
        # Detect unusual registration spikes
        data['registration_date'] = pd.to_datetime(data['created_at']).dt.date
        daily_registrations = data.groupby('registration_date').size()
        
        # Use statistical method to detect outliers
        q75, q25 = np.percentile(daily_registrations, [75, 25])
        iqr = q75 - q25
        upper_bound = q75 + (1.5 * iqr)
        
        spike_days = daily_registrations[daily_registrations > upper_bound]
        
        for date, count in spike_days.items():
            anomalies.append({
                'type': 'registration_spike',
                'date': str(date),
                'count': int(count),
                'threshold': upper_bound,
                'severity': 'high' if count > upper_bound * 2 else 'medium'
            })
        
        # Detect suspicious email patterns
        email_domains = data['email'].str.split('@').str[1].value_counts()
        suspicious_domains = email_domains[email_domains > len(data) * 0.1]  # More than 10% from same domain
        
        for domain, count in suspicious_domains.items():
            if count > 10:  # Only flag if significant number
                anomalies.append({
                    'type': 'suspicious_email_domain',
                    'domain': domain,
                    'count': int(count),
                    'percentage': (count / len(data)) * 100,
                    'severity': 'high' if count > len(data) * 0.2 else 'medium'
                })
        
        # Detect account lockout patterns
        if 'failed_login_attempts' in data.columns:
            high_failure_users = data[data['failed_login_attempts'] >= 3]
            if len(high_failure_users) > len(data) * 0.05:  # More than 5% with high failures
                anomalies.append({
                    'type': 'excessive_login_failures',
                    'affected_users': len(high_failure_users),
                    'percentage': (len(high_failure_users) / len(data)) * 100,
                    'severity': 'high'
                })
        
        results = {
            'analysis_type': 'anomaly_detection',
            'total_anomalies': len(anomalies),
            'anomalies': anomalies,
            'risk_level': 'high' if any(a['severity'] == 'high' for a in anomalies) else 'low',
            'generated_at': datetime.utcnow().isoformat()
        }
        
        logger.info(f"Anomaly detection completed: {len(anomalies)} anomalies found")
        return results
    
    def save_results(self, results: Dict[str, Any]) -> None:
        """
        Save analysis results to DynamoDB
        
        Args:
            results: Analysis results to save
        """
        try:
            # Add partition key and TTL
            results['id'] = f"{results['analysis_type']}#{datetime.utcnow().strftime('%Y%m%d%H%M%S')}"
            results['ttl'] = int((datetime.utcnow() + timedelta(days=30)).timestamp())
            
            self.results_table.put_item(Item=results)
            logger.info(f"Results saved to DynamoDB: {results['id']}")
            
        except Exception as e:
            logger.error(f"Failed to save results to DynamoDB: {str(e)}")
            raise
    
    def send_alert(self, message: str) -> None:
        """
        Send alert notification
        
        Args:
            message: Alert message to send
        """
        if SNS_TOPIC_ARN:
            try:
                sns_client.publish(
                    TopicArn=SNS_TOPIC_ARN,
                    Message=message,
                    Subject='User Analytics Alert'
                )
                logger.info("Alert sent successfully")
            except Exception as e:
                logger.error(f"Failed to send alert: {str(e)}")

def lambda_handler(event, context):
    """
    Main Lambda handler function
    
    Expected event structure:
    {
        "analysis_type": "registration_trends|engagement_metrics|anomaly_detection",
        "data_source": "s3://bucket/key" or "direct_data",
        "data": [...] (if direct_data)
    }
    """
    try:
        logger.info(f"Processing analytics request: {json.dumps(event, default=str)}")
        
        processor = UserAnalyticsProcessor()
        
        # Extract parameters
        analysis_type = event.get('analysis_type', 'registration_trends')
        data_source = event.get('data_source')
        
        # Load data
        if data_source and data_source.startswith('s3://'):
            # Load from S3
            bucket, key = data_source[5:].split('/', 1)
            response = s3_client.get_object(Bucket=bucket, Key=key)
            data = pd.read_json(response['Body'])
        elif 'data' in event:
            # Use direct data
            data = pd.DataFrame(event['data'])
        else:
            raise ValueError("No valid data source provided")
        
        logger.info(f"Loaded {len(data)} records for analysis")
        
        # Process based on analysis type
        if analysis_type == 'registration_trends':
            results = processor.process_user_registration_trends(data)
        elif analysis_type == 'engagement_metrics':
            results = processor.process_user_engagement_metrics(data)
        elif analysis_type == 'anomaly_detection':
            results = processor.detect_anomalies(data)
            
            # Send alerts for high-risk anomalies
            if results['risk_level'] == 'high':
                alert_message = f"High-risk anomalies detected: {results['total_anomalies']} issues found"
                processor.send_alert(alert_message)
        else:
            raise ValueError(f"Unsupported analysis type: {analysis_type}")
        
        # Save results
        processor.save_results(results)
        
        # Return success response
        return {
            'statusCode': 200,
            'body': json.dumps({
                'message': 'Analysis completed successfully',
                'analysis_type': analysis_type,
                'records_processed': len(data),
                'results_summary': {
                    'total_insights': len(results),
                    'generated_at': results['generated_at']
                }
            }, default=str)
        }
        
    except Exception as e:
        logger.error(f"Lambda execution failed: {str(e)}")
        
        return {
            'statusCode': 500,
            'body': json.dumps({
                'error': 'Internal server error',
                'message': str(e)
            })
        }