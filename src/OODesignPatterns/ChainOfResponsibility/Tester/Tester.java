package withoutcor.Tester;

import withoutcor.APIS.PlayVideoAPI;
import withoutcor.data.Request;

public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Request request = new Request("abc", "body", "token");
		new PlayVideoAPI().playVideo(request);
	}

}
