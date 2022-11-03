package com.sashko687.zebra.printer;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "ZebraPrinter")
public class ZebraPrinterPlugin extends Plugin {

    private ZebraPrinter implementation = new ZebraPrinter();

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void print(PluginCall call) {

        //getting the values from the app
        String ip = call.getString("ip");
        int port = call.getInt("port");
        String zpl = call.getString("zpl");


        //Do the magic native print commands
        System.out.println("start");
	    System.out.println("------------------------------------------------------------");
	    
	    // String zpl = "^XA\r\n" + 
	    // 		"^FO50,50\r\n" + 
	    // 		"^B8N,100,Y,N\r\n" + 
	    // 		"^FD1234567^FS\r\n" + 
	    // 		"^XZ";
	    // String ip = "192.168.17.239"; // PRT-LH-03

		// int port = 9100;
		
		Boolean error = false;
		try
		{
		    Socket clientSocket = null;
			DataOutputStream outToServer = null;
			try
			{
				System.out.println("Printing now...");
				clientSocket = new Socket(ip, port);
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
				outToServer.writeBytes(zpl);
				outToServer.close();
				System.out.println("send the following string: "+zpl);
				clientSocket.close();
			}
			finally
			{
				if( clientSocket != null )
				{
					outToServer.close();
					clientSocket.close();
				}
			}
		}
		catch( IOException e1 )
		{
			//throw new ZebraPrintException("Cannot print label on this printer : " + ip + ":" + port, e1);
			System.out.println("Cannot print label on this printer : " + ip + ":" + port);
			System.out.println(e1.getMessage());
            call.reject(e1.getMessage());
			error = true;
		}
	    System.out.println("------------------------------------------------------------");
		System.out.println("end");

		if(!error){
			// send a response back
			JSObject ret = new JSObject();
			ret.put("value", "Succesfully sent to printer");
			call.success(ret);
		}


    }
}
