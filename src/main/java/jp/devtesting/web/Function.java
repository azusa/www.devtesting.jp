package jp.devtesting.web;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Map;
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
                route = "{path}/",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context,
            @BindingName("path") String path) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        Map<String, String> queryParams = request.getQueryParameters();
        String c = null;
        if (queryParams != null) {
            c = queryParams.get("c");
        }
        String target = "";
        if (c != null) {
            if (c.equals("index")){
                target = "page_list.html";
            } else {
                target = c + ".html";
            }
        } else {
            // クエリーパラメーター全体を取得
            String queryString = request.getUri().getRawQuery();
            if (queryString != null && !queryString.isEmpty()){
                //C%2B%2B -> C++ -> cpp に変換
                queryString = queryString.replace("%2F", "_").replace("+", "_").replace("%2B%2B", "pp");
                target = queryString + ".html";
            }
        }

        return request.createResponseBuilder(HttpStatus.TEMPORARY_REDIRECT).header("Location", "https://archive-devtesting-jp.github.io/" + path + "/" + target).build();
    }
}
