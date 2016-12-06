package view.endpoints;

import com.google.gson.Gson;
import logic.StudentController;
import security.Digester;
import shared.ReviewDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by Kasper on 19/10/2016.
 */

@Path("/api/student")
public class StudentEndpoint extends UserEndpoint {

    @POST
    @Consumes("application/json")
    @Path("/review")
    public Response addReview(String json) {

        Gson gson = new Gson();
        ReviewDTO review = new Gson().fromJson(json, ReviewDTO.class);

        StudentController studentCtrl = new StudentController();
        boolean isAdded = studentCtrl.addReview(review);

        if (isAdded) {
            String toJson = gson.toJson(Digester.encrypt(gson.toJson(isAdded)));

            return successResponse(200, toJson);

        } else {
            return errorResponse(404, "Failed. Couldn't get reviews.");
        }
    }


    @OPTIONS
    @Path("/review/")
    public Response deleteReview() {
        return Response
                .status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "Content-Type")
                .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE")
                .build();
    }

    @DELETE
    @Consumes("application/json")
    @Path("/review/")
    public Response deleteReview(String data) {
        Gson gson = new Gson();

        ReviewDTO review = gson.fromJson(data, ReviewDTO.class);
        StudentController studentCtrl = new StudentController();

        boolean isDeleted = studentCtrl.softDeleteReview(review.getUserId(), review.getId());

        if (isDeleted) {
            String toJson = gson.toJson(Digester.encrypt(gson.toJson(isDeleted)));

            return successResponse(200, toJson);
        } else {
            return errorResponse(404, "Failed. Couldn't delete the chosen review.");
        }
    }

    protected Response errorResponse(int status, String message) {

        return Response.status(status).entity(new Gson().toJson(Digester.encrypt("{\"message\": \"" + message + "\"}"))).build();
    }

    protected Response successResponse(int status, Object data) {
        Gson gson = new Gson();

        //Adding response headers to enable CORS in the Chrome browser
        return Response.status(status).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "Content-Type").header("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE").entity(gson.toJson(data)).build();

        // return Response.status(status).entity(gson.toJson(data)).build();
        //return Response.status(status).entity(gson.toJson(Digester.encrypt(gson.toJson(data)))).build();

    }
}

