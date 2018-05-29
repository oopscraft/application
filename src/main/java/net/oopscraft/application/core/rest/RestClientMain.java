package net.oopscraft.application.core.rest;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.TextTableBuilder;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.core.rest.example.ExampleClient;



public class RestClientMain extends JFrame { 
	
	private static final long serialVersionUID = -3361457504257128292L; 
	
	FlowLayout flow = new FlowLayout(FlowLayout.LEFT); 
	Font font = new Font("Courier New",Font.ITALIC,12); 
	JComboBox<String> apiList = null; 
	JTextArea contextTextArea = new JTextArea(15,110); 
	JTextArea testTextArea = new JTextArea(10,110); 
	JTextArea resultTextArea = new JTextArea(15,110); 
	
	public static void main(String[] args) throws Exception {
		String restRequestXml = "/" + RestClientMain.class.getPackage().getName().replaceAll("\\.", "/") + "/RestClient.xml";
		String host = "http://marketdata.krx.co.kr";
		new RestClientMain(ExampleClient.class, restRequestXml, host); 
	} 
	
	public RestClientMain(final Class<?> clazz, final String restRequestXml, final String host) throws Exception { 
		super.setLayout(flow); 
		
		// API 
		RestRequestFactory requestFactory = RestRequestFactory.getInstance(restRequestXml); 
		List<RestRequest> requestList = requestFactory.getRestRequestList(); 
		Vector<String> apis = new Vector<String>(); 
		for(RestRequest request : requestList){ 
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
					RestRequestFactory requestFactory = RestRequestFactory.getInstance(restRequestXml); 
					RestRequest request = requestFactory.getRestRequest(id); 
					contextTextArea.setText(TextTableBuilder.build(request)); 
					testTextArea.setText(request.getTest()); 
					resultTextArea.setText(""); 
				}catch(Exception e){ 
					e.printStackTrace(System.err); 
				} 
			} 
		}); 

		// request button
		JButton requestButton = new JButton("Request"); 
		requestButton.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent evt) { 
				try { 
					String selectedItem = (String) apiList.getSelectedItem(); 
					String id = selectedItem.split(":")[0];
					
					// creates RestClient instance
					Object restClient = RestClientFactory.getRestClient(clazz, restRequestXml, host);
					ValueMap params = null; 
					if(testTextArea.getText() != null) { 
						params = JsonConverter.convertJsonToObject(testTextArea.getText(), ValueMap.class); 
					} 
					resultTextArea.setText(""); 
					
					// call method via reflection.
					Method method = null;
					for(Method element : clazz.getDeclaredMethods()) {
						if(element.getName().equals(id)) {
							method = element;
							break;
						}
					}
					
					// setting parameter from interface annotations.
					Vector<Object> args = new Vector<Object>();
					Annotation[][] annotationsArray = method.getParameterAnnotations();
					for(int i = 0; i < annotationsArray.length; i ++) {
						Annotation annotation = annotationsArray[i][0];
						RestParam restParam = (RestParam) annotation;
						String name = restParam.value();
						Object value = params.get(name);;
						args.add(value);
			        }
					
					// call request
					Object response = method.invoke(restClient, args.toArray(new Object[args.size()-1]));
					String result = null;
					if(response instanceof Object) {
						result = JsonConverter.convertObjectToJson(response);
					}else {
						result = (String) response;
					}
					resultTextArea.setText(result); 
				}catch(Exception e){ 
					e.printStackTrace(System.err);
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	} 
	

}
