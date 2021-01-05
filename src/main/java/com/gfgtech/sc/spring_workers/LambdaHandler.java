package com.gfgtech.sc.spring_workers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.gfgtech.sc.spring_workers.domain.LambdaResponse;
import java.util.Map;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author Wolfram Huesken
 * @author Thanh Vo
 */
public class LambdaHandler implements RequestHandler<Map<String, Object>, Object> {
    public LambdaHandler() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(SpringWorkersApplication.class)
            .logStartupInfo(true)
            .web(WebApplicationType.NONE);
        builder.build().run();
    }

    @Override
    public Object handleRequest(Map<String, Object> event, Context context) {
        return LambdaResponse.builder().status("success").build();
    }
}
