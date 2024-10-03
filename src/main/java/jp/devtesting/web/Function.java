package jp.devtesting.web;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("Index")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        final String c = request.getQueryParameters().get("c");
        String target = "";
        if (c != null) {
            if (c.equals("index")){
                target = "page_list.html";
            } else {
                target = c + ".html";
            }
        }
        // クエリーパラメーター全体を取得
        final String queryString = request.getUri().getQuery();

        if (queryString != null) {
            target = queryString + ".html";
        }
        return request.createResponseBuilder(HttpStatus.PERMANENT_REDIRECT).header("Location", "https://archive-devtesting-jp.github.io/tddbc/" + target).build();
    }
}
