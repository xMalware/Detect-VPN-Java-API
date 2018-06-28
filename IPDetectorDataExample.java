package us.ipdetector.example;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;

/**
 * 
 * This class is an example that allows to know IP details (with IP VPN check) using the IPDetector API.
 * 
 * (1) Good IP address = Business / Home connection
 * (0) Bad IP address = VPN/Host Provider/Proxy
 * 
 * You can use the free offer (30 requests per minute) or the paid offer.
 * Create your API key at https://ipdetector.info
 * 
 * @author xMalware
 *
 */
public class IPDetectorDataExample
{

	// Application name (User agent) : Change it!
	private final static String		APPLICATION_NAME	= "IPDetectorDataExample";
	// Set up your API key on IPDetector.info : Change it!
	private final static String		API_KEY			= "Your API key";
	// If you want to pretty print data: new GsonBuilder().setPrettyPrinting().create();
	private final static Gson		gson			= new Gson();

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Test with 8.8.8.8
		final String ipAddress = "8.8.8.8";
		// Get IP data
		final IPDetectorObject ipData = getIPData(ipAddress);

		// If the IP data object is null
		if (ipData == null)
		{
			// So we say we can't get the data
			System.out.println("Unable to get IP data from " + ipAddress + ".");
		}
		// If the IP data object isn't null
		else
		{
			// Give the IP address
			System.out.println("IP address: " + ipData.ip);
			// Give the hostname
			System.out.println("Hostname: " + ipData.hostname);
			// Give the country name
			System.out.println("Country name: " + ipData.countryName);
			// Give the country ISO
			System.out.println("Country ISO: " + ipData.countryIso);
			// Give the continent name
			System.out.println("Continent name: " + ipData.continentName);
			// Give the continent code
			System.out.println("Continent code: " + ipData.continentCode);
			// Give the response time
			System.out.println("Response time: " + ipData.responseTime);
			// Give the ASN id
			System.out.println("ASN id: " + ipData.asnId);
			// Give the ASN name
			System.out.println("ASN name: " + ipData.asnName);
			// Give the organization
			System.out.println("Organization: " + ipData.organization);
			// Give the ISP
			System.out.println("ISP: " + ipData.isp);
			// Give if the IP address is "good" or not
			System.out.println("Good IP: " + (ipData.goodIp == 1 ? "Yes" : "No"));
		}
	}

	/**
	 * Get IP data
	 * @param ip
	 * @return
	 */
	private static IPDetectorObject getIPData(String ip)
	{
		// We get the source code
		final String sourceCode = fetchSourceCode(ip, API_KEY);
		// If there is an error in the answer (e.g. rate limited)
		if (sourceCode.toLowerCase().contains("\"error\":"))
		{
			// Uncomment if you want to print the error
			// System.out.println(sourceCode);
			// So we return null
			return null;
		}
		// Deserialize data in an object
		return gson.fromJson(ip, IPDetectorObject.class);
	}

	/**
	 * Fetch the source code
	 * 
	 * @param link
	 * @param apiKey
	 * @return
	 */
	public static String fetchSourceCode(String ip, String apiKey) {
		// Try to
		try {

			// Set the user agent
			System.setProperty("http.agent", APPLICATION_NAME);
			// Create url
			final URL url = new URL("https://api.ipdetector.info/" + ip);
			// Do the connection
			final URLConnection con = url.openConnection();
			// Set the user agent
			con.setRequestProperty("User-Agent", APPLICATION_NAME);
			// Set API key
			con.setRequestProperty("API-Key", apiKey);
			// Get input stream
			final InputStream in = con.getInputStream();
			// Get the output stream
			final ByteArrayOutputStream result = new ByteArrayOutputStream();
			// Create a new buffer
			final byte[] buffer = new byte[1024];
			// Buffer length
			int length;
			// While the length isn't equal to -1.
			while ((length = in.read(buffer)) != -1) {
				// We write the data
				result.write(buffer, 0, length);
			}
			// Return the result in UTF-8
			return result.toString("UTF-8");
		}
		// Error case
		catch (Exception error)
		{
			// Print the error
			error.printStackTrace();
			// Return null
			return null;
		}
	}

	/**
	 * IP Detector object
	 * @author xMalware
	 */
	public class IPDetectorObject
	{

		// IP address
		public String	ip;
		// Hostname
		public String	hostname;
		// Country name
		public String	countryName;
		// Country ISO
		public String	countryIso;
		// Continent name
		public String	continentName;
		// Contient code
		public String	continentCode;
		// Response time
		public long	responseTime;
		// ASN Id
		public int	asnId;
		// ASN name
		public String	asnName;
		// Organization
		public String	organization;
		// ISP
		public String	isp;
		// Is this a Good IP?
		// 0 = No (VPN/host provider/Proxy)
		// 1 = Yes
		public int	goodIp;

	}

}
