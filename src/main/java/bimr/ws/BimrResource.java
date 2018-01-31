package bimr.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@Path("/helloworld")
public class BimrResource {

	@Path("/helloworld") // --> Resource Identifier public class HelloWorldResource {
	@GET // --> Process HTTP GET requests @Produces("text/plain") // --> MIME Media type
	public String getMessage() {
		System.out.println("here");
		return "<html> <p>Hello World</p></html>";
	}

//	@PUT
//	@Consumes("text/plain")
//	public void setMessage(String msg) {
//
//	}
}


//@Path("/message")
//public class JSONService {
//
//	@GET
//	@Path("/{param}")
//	@Produces("application/json")
//	public Response printMessage(@PathParam("param") String msg) {
//		String result = "Restful example: " + msg;
//		return Response.status(200).entity(result).build();
//	}
//
//}