package com.smartcampus.api.endpoints;
import com.smartcampus.api.response.ErrorResponse;
import com.smartcampus.core.errors.LinkedResourceNotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        return Response.status(422)
                .entity(new ErrorResponse(422, ex.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
