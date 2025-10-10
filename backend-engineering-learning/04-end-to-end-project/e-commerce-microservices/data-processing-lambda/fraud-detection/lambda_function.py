"""
Fraud Detection Lambda Function

This Lambda function analyzes user behavior patterns to detect potential fraud
and security threats in real-time. It demonstrates machine learning integration
and event-driven security monitoring.

Key Features:
- Real-time fraud scoring
- Behavioral anomaly detection
- Risk assessment and alerting
- Integration with security systems
- Scalable event processing

Architecture Benefits:
- Serverless scaling based on load
- Cost-effective per-request pricing
- Integration with AWS security services
- Real-time response capabilities
"""

import json
import boto3
import pandas as pd
import numpy as np
from datetime import datetime, timedelta
from typing import Dict, List, Any, Tuple
import logging
import os
import hashlib
from collections import defaultdict
import ipaddress

# Configure logging
logger = logging.getLogger()
logger.setLevel(logging.INFO)

# AWS clients
dynamodb = boto3.resource('dynamodb')
sns_client = boto3.client('sns')
s3_client = boto3.client('s3')
cognito_client = boto3.client('cognito-idp')

# Environment variables
FRAUD_SCORES_TABLE = os.environ.get('FRAUD_SCORES_TABLE', 'fraud-detection-scores')
USER_PROFILES_TABLE = os.environ.get('USER_PROFILES_TABLE', 'user-behavior-profiles')
SECURITY_ALERTS_TOPIC = os.environ.get('SECURITY_ALERTS_TOPIC')
HIGH_RISK_THRESHOLD = float(os.environ.get('HIGH_RISK_THRESHOLD', '0.8'))
MEDIUM_RISK_THRESHOLD = float(os.environ.get('MEDIUM_RISK_THRESHOLD', '0.5'))

class FraudDetectionEngine:
    """
    Core fraud detection engine with multiple detection algorithms
    """
    
    def __init__(self):
        self.fraud_scores_table = dynamodb.Table(FRAUD_SCORES_TABLE)
        self.user_profiles_table = dynamodb.Table(USER_PROFILES_TABLE)
        
        # Risk factors and their weights
        self.risk_weights = {
            'velocity_score': 0.25,      # Rapid successive actions
            'location_score': 0.20,      # Unusual location patterns
            'device_score': 0.15,        # New or suspicious devices
            'behavioral_score': 0.20,    # Unusual behavior patterns
            'account_age_score': 0.10,   # Account age risk
            'network_score': 0.10        # Network-based risks
        }
    
    def calculate_velocity_score(self, user_events: List[Dict]) -> float:
        """
        Calculate risk score based on event velocity
        
        Args:
            user_events: List of user events with timestamps
            
        Returns:
            Risk score between 0 and 1
        """
        if not user_events:
            return 0.0
        
        # Sort events by timestamp
        events = sorted(user_events, key=lambda x: x['timestamp'])
        
        # Calculate time intervals between events
        intervals = []
        for i in range(1, len(events)):
            prev_time = datetime.fromisoformat(events[i-1]['timestamp'].replace('Z', '+00:00'))
            curr_time = datetime.fromisoformat(events[i]['timestamp'].replace('Z', '+00:00'))
            interval = (curr_time - prev_time).total_seconds()
            intervals.append(interval)
        
        if not intervals:
            return 0.0
        
        # Detect suspicious patterns
        avg_interval = np.mean(intervals)
        min_interval = min(intervals)
        
        # Very rapid succession (< 1 second between actions)
        if min_interval < 1:
            return 0.9
        
        # Unusually rapid average (< 5 seconds average)
        if avg_interval < 5:
            return 0.7
        
        # High frequency actions (< 30 seconds average)
        if avg_interval < 30:
            return 0.4
        
        return 0.1
    
    def calculate_location_score(self, user_events: List[Dict], user_profile: Dict) -> float:
        """
        Calculate risk score based on location patterns
        
        Args:
            user_events: List of user events with location data
            user_profile: Historical user profile data
            
        Returns:
            Risk score between 0 and 1
        """
        current_locations = set()
        for event in user_events:
            if 'ip_address' in event:
                # In production, use IP geolocation service
                # For demo, we'll simulate location detection
                ip_hash = hashlib.md5(event['ip_address'].encode()).hexdigest()
                simulated_country = ip_hash[:2]  # Simplified country code
                current_locations.add(simulated_country)
        
        if not current_locations:
            return 0.0
        
        # Get historical locations from user profile
        historical_locations = set(user_profile.get('common_locations', []))
        
        # Check for new locations
        new_locations = current_locations - historical_locations
        
        if not historical_locations:
            # New account, moderate risk
            return 0.3
        
        if new_locations:
            # New location detected
            if len(new_locations) > 1:
                # Multiple new locations
                return 0.8
            else:
                # Single new location
                return 0.5
        
        return 0.1
    
    def calculate_device_score(self, user_events: List[Dict], user_profile: Dict) -> float:
        """
        Calculate risk score based on device patterns
        
        Args:
            user_events: List of user events with device data
            user_profile: Historical user profile data
            
        Returns:
            Risk score between 0 and 1
        """
        current_devices = set()
        for event in user_events:
            if 'user_agent' in event:
                # Create device fingerprint from user agent
                device_fingerprint = hashlib.md5(event['user_agent'].encode()).hexdigest()[:16]
                current_devices.add(device_fingerprint)
        
        if not current_devices:
            return 0.0
        
        # Get historical devices from user profile
        historical_devices = set(user_profile.get('known_devices', []))
        
        # Check for new devices
        new_devices = current_devices - historical_devices
        
        if not historical_devices:
            # New account
            return 0.2
        
        if new_devices:
            if len(new_devices) > 1:
                # Multiple new devices
                return 0.7
            else:
                # Single new device
                return 0.4
        
        return 0.1
    
    def calculate_behavioral_score(self, user_events: List[Dict], user_profile: Dict) -> float:
        """
        Calculate risk score based on behavioral patterns
        
        Args:
            user_events: List of user events
            user_profile: Historical user profile data
            
        Returns:
            Risk score between 0 and 1
        """
        if not user_events:
            return 0.0
        
        # Analyze event types and patterns
        event_types = [event.get('event_type', 'unknown') for event in user_events]
        event_type_counts = defaultdict(int)
        for event_type in event_types:
            event_type_counts[event_type] += 1
        
        # Get historical behavioral patterns
        historical_patterns = user_profile.get('behavioral_patterns', {})
        
        risk_indicators = []
        
        # Check for unusual event frequencies
        for event_type, count in event_type_counts.items():
            historical_avg = historical_patterns.get(f'{event_type}_avg', 0)
            if historical_avg > 0:
                deviation = abs(count - historical_avg) / historical_avg
                if deviation > 2.0:  # More than 200% deviation
                    risk_indicators.append(0.6)
                elif deviation > 1.0:  # More than 100% deviation
                    risk_indicators.append(0.3)
        
        # Check for unusual event sequences
        if len(event_types) > 1:
            # Look for repetitive patterns that might indicate automation
            sequences = []
            for i in range(len(event_types) - 1):
                sequences.append(f"{event_types[i]}->{event_types[i+1]}")
            
            sequence_counts = defaultdict(int)
            for seq in sequences:
                sequence_counts[seq] += 1
            
            # Flag high repetition
            max_repetition = max(sequence_counts.values()) if sequence_counts else 0
            if max_repetition > len(sequences) * 0.7:  # More than 70% same sequence
                risk_indicators.append(0.8)
        
        # Return average risk or 0 if no indicators
        return np.mean(risk_indicators) if risk_indicators else 0.1
    
    def calculate_account_age_score(self, user_profile: Dict) -> float:
        """
        Calculate risk score based on account age
        
        Args:
            user_profile: User profile data
            
        Returns:
            Risk score between 0 and 1
        """
        created_at = user_profile.get('created_at')
        if not created_at:
            return 0.5
        
        try:
            created_date = datetime.fromisoformat(created_at.replace('Z', '+00:00'))
            account_age_days = (datetime.now(created_date.tzinfo) - created_date).days
            
            # New accounts are higher risk
            if account_age_days < 1:
                return 0.8
            elif account_age_days < 7:
                return 0.6
            elif account_age_days < 30:
                return 0.4
            elif account_age_days < 90:
                return 0.2
            else:
                return 0.1
                
        except (ValueError, TypeError):
            return 0.5
    
    def calculate_network_score(self, user_events: List[Dict]) -> float:
        """
        Calculate risk score based on network patterns
        
        Args:
            user_events: List of user events with network data
            
        Returns:
            Risk score between 0 and 1
        """
        ip_addresses = set()
        for event in user_events:
            if 'ip_address' in event:
                ip_addresses.add(event['ip_address'])
        
        if not ip_addresses:
            return 0.0
        
        risk_indicators = []
        
        # Check for suspicious IP patterns
        for ip in ip_addresses:
            try:
                ip_obj = ipaddress.ip_address(ip)
                
                # Check for private/internal IPs in unexpected contexts
                if ip_obj.is_private and len(ip_addresses) > 1:
                    risk_indicators.append(0.3)
                
                # Check for localhost (should not happen in production)
                if ip_obj.is_loopback:
                    risk_indicators.append(0.9)
                    
                # Simple check for suspicious IP ranges (in production, use threat intelligence)
                if ip.startswith('10.0.0.') or ip.startswith('192.168.1.'):
                    risk_indicators.append(0.2)
                    
            except ValueError:
                # Invalid IP address
                risk_indicators.append(0.7)
        
        # Multiple IPs in short time frame
        if len(ip_addresses) > 3:
            risk_indicators.append(0.6)
        elif len(ip_addresses) > 1:
            risk_indicators.append(0.3)
        
        return np.mean(risk_indicators) if risk_indicators else 0.1
    
    def calculate_composite_fraud_score(self, user_events: List[Dict], user_profile: Dict) -> Dict[str, Any]:
        """
        Calculate comprehensive fraud score using all risk factors
        
        Args:
            user_events: List of user events
            user_profile: User profile data
            
        Returns:
            Dictionary with detailed fraud analysis
        """
        logger.info(f"Calculating fraud score for {len(user_events)} events")
        
        # Calculate individual risk scores
        velocity_score = self.calculate_velocity_score(user_events)
        location_score = self.calculate_location_score(user_events, user_profile)
        device_score = self.calculate_device_score(user_events, user_profile)
        behavioral_score = self.calculate_behavioral_score(user_events, user_profile)
        account_age_score = self.calculate_account_age_score(user_profile)
        network_score = self.calculate_network_score(user_events)
        
        # Calculate weighted composite score
        composite_score = (
            velocity_score * self.risk_weights['velocity_score'] +
            location_score * self.risk_weights['location_score'] +
            device_score * self.risk_weights['device_score'] +
            behavioral_score * self.risk_weights['behavioral_score'] +
            account_age_score * self.risk_weights['account_age_score'] +
            network_score * self.risk_weights['network_score']
        )
        
        # Determine risk level
        if composite_score >= HIGH_RISK_THRESHOLD:
            risk_level = 'HIGH'
        elif composite_score >= MEDIUM_RISK_THRESHOLD:
            risk_level = 'MEDIUM'
        else:
            risk_level = 'LOW'
        
        result = {
            'composite_score': round(composite_score, 3),
            'risk_level': risk_level,
            'risk_factors': {
                'velocity_score': round(velocity_score, 3),
                'location_score': round(location_score, 3),
                'device_score': round(device_score, 3),
                'behavioral_score': round(behavioral_score, 3),
                'account_age_score': round(account_age_score, 3),
                'network_score': round(network_score, 3)
            },
            'analysis_timestamp': datetime.utcnow().isoformat(),
            'events_analyzed': len(user_events)
        }
        
        logger.info(f"Fraud analysis complete: {risk_level} risk (score: {composite_score:.3f})")
        return result
    
    def save_fraud_score(self, user_id: str, fraud_analysis: Dict[str, Any]) -> None:
        """
        Save fraud analysis results to DynamoDB
        
        Args:
            user_id: User identifier
            fraud_analysis: Fraud analysis results
        """
        try:
            item = {
                'user_id': user_id,
                'timestamp': fraud_analysis['analysis_timestamp'],
                'composite_score': fraud_analysis['composite_score'],
                'risk_level': fraud_analysis['risk_level'],
                'risk_factors': fraud_analysis['risk_factors'],
                'events_analyzed': fraud_analysis['events_analyzed'],
                'ttl': int((datetime.utcnow() + timedelta(days=90)).timestamp())
            }
            
            self.fraud_scores_table.put_item(Item=item)
            logger.info(f"Fraud score saved for user: {user_id}")
            
        except Exception as e:
            logger.error(f"Failed to save fraud score: {str(e)}")
            raise
    
    def send_security_alert(self, user_id: str, fraud_analysis: Dict[str, Any]) -> None:
        """
        Send security alert for high-risk users
        
        Args:
            user_id: User identifier
            fraud_analysis: Fraud analysis results
        """
        if SECURITY_ALERTS_TOPIC and fraud_analysis['risk_level'] in ['HIGH', 'MEDIUM']:
            try:
                message = {
                    'alert_type': 'FRAUD_DETECTION',
                    'user_id': user_id,
                    'risk_level': fraud_analysis['risk_level'],
                    'composite_score': fraud_analysis['composite_score'],
                    'top_risk_factors': self._get_top_risk_factors(fraud_analysis['risk_factors']),
                    'timestamp': fraud_analysis['analysis_timestamp']
                }
                
                sns_client.publish(
                    TopicArn=SECURITY_ALERTS_TOPIC,
                    Message=json.dumps(message),
                    Subject=f"Fraud Alert: {fraud_analysis['risk_level']} Risk User {user_id}"
                )
                
                logger.info(f"Security alert sent for user {user_id} - {fraud_analysis['risk_level']} risk")
                
            except Exception as e:
                logger.error(f"Failed to send security alert: {str(e)}")
    
    def _get_top_risk_factors(self, risk_factors: Dict[str, float], top_n: int = 3) -> List[Dict[str, Any]]:
        """
        Get top risk factors sorted by score
        
        Args:
            risk_factors: Dictionary of risk factor scores
            top_n: Number of top factors to return
            
        Returns:
            List of top risk factors with scores
        """
        sorted_factors = sorted(risk_factors.items(), key=lambda x: x[1], reverse=True)
        return [{'factor': factor, 'score': score} for factor, score in sorted_factors[:top_n]]

def lambda_handler(event, context):
    """
    Main Lambda handler for fraud detection
    
    Expected event structure:
    {
        "user_id": "user123",
        "events": [...],
        "user_profile": {...}
    }
    """
    try:
        logger.info(f"Processing fraud detection request: {json.dumps(event, default=str)}")
        
        # Extract parameters
        user_id = event.get('user_id')
        user_events = event.get('events', [])
        user_profile = event.get('user_profile', {})
        
        if not user_id:
            raise ValueError("user_id is required")
        
        if not user_events:
            logger.warning(f"No events provided for user {user_id}")
            return {
                'statusCode': 200,
                'body': json.dumps({
                    'user_id': user_id,
                    'message': 'No events to analyze',
                    'risk_level': 'LOW',
                    'composite_score': 0.0
                })
            }
        
        # Initialize fraud detection engine
        engine = FraudDetectionEngine()
        
        # Perform fraud analysis
        fraud_analysis = engine.calculate_composite_fraud_score(user_events, user_profile)
        fraud_analysis['user_id'] = user_id
        
        # Save results
        engine.save_fraud_score(user_id, fraud_analysis)
        
        # Send alerts if necessary
        engine.send_security_alert(user_id, fraud_analysis)
        
        # Return results
        return {
            'statusCode': 200,
            'body': json.dumps({
                'user_id': user_id,
                'fraud_analysis': fraud_analysis,
                'message': 'Fraud analysis completed successfully'
            }, default=str)
        }
        
    except Exception as e:
        logger.error(f"Fraud detection failed: {str(e)}")
        
        return {
            'statusCode': 500,
            'body': json.dumps({
                'error': 'Fraud detection failed',
                'message': str(e)
            })
        }