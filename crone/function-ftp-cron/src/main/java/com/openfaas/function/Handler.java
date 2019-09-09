package com.openfaas.function;

import com.openfaas.model.IHandler;
import com.openfaas.model.IResponse;
import com.openfaas.model.IRequest;
import com.openfaas.model.Response;

import io.javalin.Javalin;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


public class Handler implements com.openfaas.model.IHandler {
	static String parentDirectory = "/test";
	static FTPClient ftpClient = new FTPClient();

    public IResponse Handle(IRequest req) {
        Response res = new Response();
	
	//adding the port number which are same as my upstream url in docker file
	Javalin javalin = Javalin.create().start(8082);

	javalin.get("/", ctx -> {

				
				ObjectMapper mapper = new ObjectMapper();
				ObjectNode node = mapper.createObjectNode();
				ArrayNode arrayNode = mapper.createArrayNode();

				FTPFile[] list = ftpClient.listFiles(parentDirectory);
				for (FTPFile file : list) {

					String name = file.getName();
					// Adding the file name to array node
					arrayNode.add(name);

				}

				(node).set("Files", arrayNode);
				String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
				System.out.println("the json data from the ftpread function" + jsonString);
				ctx.result(jsonString);
			
		});
	    res.setBody("Hello, world!");

	    return res;
    }
}
