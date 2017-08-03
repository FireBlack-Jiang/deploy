package com.myabc.Third;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class SocketThirdCaller implements ThirdCaller {

	public String call(String thirdPkg, Object... args) {
		try {
			Socket socket=new Socket("10.52.8.124",8015);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.write(thirdPkg.getBytes("gbk"));
			dos.flush();
			socket.shutdownOutput();			

//			BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			DataInputStream dis=new DataInputStream(socket.getInputStream());
			InputStreamReader ir=new InputStreamReader(socket.getInputStream(),"gbk");
			char[] ch=new char[2048];
			StringBuilder sb=new StringBuilder();
			socket.setSoTimeout(60000);
			
			int readCount=ir.read(ch);
			sb.append(ch, 0, readCount);
			
//			byte[] retByte=new byte[8];
//			dis.readFully(retByte);
//			System.out.println(Arrays.toString(retByte));
//			
//			socket.setSoTimeout(60000);
//			String ret=br.readLine();
//			String ret=new String(retByte, "UTF-8");                                                     
			
//			br.close();
			dos.close();
			socket.close();
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
