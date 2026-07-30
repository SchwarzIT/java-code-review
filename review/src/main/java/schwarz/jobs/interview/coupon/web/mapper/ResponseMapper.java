package schwarz.jobs.interview.coupon.web.mapper;

import java.util.List;

/**
 * Converts a domain object into the DTO returned to API clients.
 */
public interface ResponseMapper<I, R> {

    R toResponse(I input);

    List<R> toResponses(List<I> inputs);

}
