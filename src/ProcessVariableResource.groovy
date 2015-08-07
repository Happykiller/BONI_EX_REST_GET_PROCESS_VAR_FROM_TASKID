import groovy.json.JsonBuilder

import org.bonitasoft.console.common.server.page.*
import org.bonitasoft.console.common.server.utils.ProcessAccessor;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;

import javax.servlet.http.HttpServletRequest

public class Get implements RestApiController {

    @Override
    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {
		logger.info("START REST API EXTENSION PROCESS VARIABLE");
		Map<String, String> response = [:]
		Logger logger = restApiUtil.logger;
		
		Long taskId = (Long) request.getParameter("taskId");
		logger.info("taskId=......" + taskId);
		String processVariableName = (String) request.getParameter("processVariableName");
		APISession session: pageContext.getApiSession();
		ProcessAPI processA = TenantAPIAccessor.getProcessAPI(session);
		
		//taskId -> caseId
		Long caseId = processA.getProcessInstanceIdFromActivityInstanceId(taskId);
		def processValue = processA.getProcessDataInstance(processVariableName, caseId);
		//caseId -> process value
		
		
        response.put "response", processValue
        response.putAll request.parameterMap
				
		
        apiResponseBuilder.with {
            withResponse new JsonBuilder(response).toPrettyString()
            build()
        }
    }
}
