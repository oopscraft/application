package net.oopscraft.application.core;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HttpClientMain extends JFrame { 
	private static final long serialVersionUID = -3361457504257128292L; 
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientMain.class); 
	FlowLayout flow = new FlowLayout(FlowLayout.LEFT); 
	Font font = new Font("Courier New",Font.ITALIC,12); 
	JComboBox<String> apiList = null; 
	JTextArea contextTextArea = new JTextArea(15,110); 
	JTextArea testTextArea = new JTextArea(10,110); 
	JTextArea resultTextArea = new JTextArea(15,110); 
	
	public static void main(String[] args) throws Exception { 
		String host = args[0]; 
		String resourceXml = args[1]; 
		new HttpClientMain(host, resourceXml); 
	} 
	
	public HttpClientMain(final String host, final String resourceXml) throws Exception { 
		super(host + " [" + resourceXml + "]"); 
		super.setLayout(flow); 
		// API 紐⑸줉 
		HttpRequestFactory requestFactory = HttpRequestFactory.getInstance(resourceXml); 
		List<HttpRequest> requestList = requestFactory.getHttpRequestList(); 
		Vector<String> apis = new Vector<String>(); 
		for(HttpRequest request : requestList){ 
			apis.add(request.getId() + ": [" + request.getName()+"]"); 
		} 
		apiList = new JComboBox<String>(apis); 
		add(new JLabel("MA-SERVER-OPENAPI")); 
		add(apiList); apiList.setSelectedIndex(-1); 
		apiList.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent evt) { 
				String selectedItem = (String) apiList.getSelectedItem(); 
				String id = selectedItem.split(":")[0].trim(); 
				try { 
					HttpRequestFactory requestFactory = HttpRequestFactory.getInstance(resourceXml); 
					HttpRequest request = requestFactory.getHttpRequest(id); 
					contextTextArea.setText(TextTableBuilder.build(request)); 
					testTextArea.setText(request.getTest()); 
					resultTextArea.setText(""); 
				}catch(Exception e){ 
					LOGGER.error(e.getMessage(),e); 
				} 
			} 
		}); 

		// request button
		JButton requestButton = new JButton("?占쏙옙占�?"); 
		requestButton.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent evt) { 
				try { 
					String selectedItem = (String) apiList.getSelectedItem(); 
					String id = selectedItem.split(":")[0]; 
					HttpClient client = new HttpClient(host); 
					ValueMap params = null; 
					if(testTextArea.getText() != null) { 
						params = JsonConverter.convertJsonToObject(testTextArea.getText(), ValueMap.class); 
					} 
					resultTextArea.setText(""); 
					HttpRequestFactory requestFactory = HttpRequestFactory.getInstance(resourceXml); 
					HttpRequest request = requestFactory.getHttpRequest(id); 
					String response = client.request(request,params,null,0); 
					response = convertFormedString(response); 
					resultTextArea.setText(response); 
				}catch(Exception e){ 
					resultTextArea.setText(e.getMessage()); 
				} 
			} 
		}); 
		
		add(requestButton); 
		
		// Request message 
		add(new JLabel("Payload ")); 
		contextTextArea.setEditable(false); 
		contextTextArea.setFont(font); 
		add(new JScrollPane(contextTextArea)); 
		
		// Parameter 
		add(new JLabel("Parameter(Json) ")); 
		testTextArea.setEditable(true); 
		testTextArea.setFont(font); 
		add(new JScrollPane(testTextArea)); 

		add(new JLabel("Respose Message ")); 
		resultTextArea.setEditable(false); 
		resultTextArea.setFont(font); 
		add(new JScrollPane(resultTextArea)); 

		setSize(800, 800); 
		setVisible(true); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 	// x踰꾪듉 ?占쏙옙?占쏙옙 
	} 
	
	private static String convertFormedString(String str) { 
		String formedString = null; 
		try { 
			ValueMap map = JsonConverter.convertJsonToObject(str, ValueMap.class); 
			formedString = JsonConverter.convertObjectToJson(map); 
		}catch(Exception ignore){ 
			formedString = str; 
		} 
		return formedString; 
	} 
	
}
