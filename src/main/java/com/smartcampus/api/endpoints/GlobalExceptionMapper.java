package com.smartcampus.api.endpoints;
import com.smartcampus.api.response.ErrorResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable t) {
        if (t instanceof javax.ws.rs.WebApplicationException) {
            return ((javax.ws.rs.WebApplicationException) t).getResponse();
        }
        return Response.status(500)
                .entity(new ErrorResponse(500, "An unexpected error occurred."))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
