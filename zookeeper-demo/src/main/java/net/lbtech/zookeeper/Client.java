package net.lbtech.zookeeper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

/*
 * 监听变化
 */
public class Client {

	private ZkClient zk;
	private String path = "/test";
	
	public Client(String url) {
		super();
		this.zk = new ZkClient(url,5000,5000,new ZkSerializer() {
			
			  @Override
		        public byte[] serialize(Object data) throws ZkMarshallingError {
		            try {
		                return ((String)data).getBytes("UTF-8");
		            } catch (UnsupportedEncodingException e) {
		                throw new RuntimeException(e);
		            }
		        }

		        @Override
		        public Object deserialize(byte[] bytes) throws ZkMarshallingError {
		            if (bytes == null)
		                return null;
		            try {
		                return new String(bytes, "UTF-8");
		            } catch (UnsupportedEncodingException e) {
		                throw new RuntimeException(e);
		            }
		        }
		});//多个用，分隔
	}
	public void dispose(){
		zk.close();
		System.out.println("zkclient closed!");
	}
	public void listener() throws IOException{
		zk.subscribeDataChanges(path, new IZkDataListener(){
			@Override
			public void handleDataChange(String dataPath, Object data)
					throws Exception {
				System.out.println("node data changed!");
				System.out.println("node=>" + dataPath);
				System.out.println("data=>" + data);
				System.out.println("--------------");
			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("node data deleted!");
				System.out.println("s=>" + dataPath);
				System.out.println("--------------");
			}});
		
		System.out.println("任意键结束.....");
		System.in.read();
	}
	public static void main(String[] args) throws IOException {
		new Client("10.4.1.206:2181").listener();
	}
}
