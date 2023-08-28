package telran.net;

public class CalculatorProtocol implements ApplProtocol {
    @Override
    public Response getResponse(Request request) {
       String requestType = request.requestType();
        Double[] reQuestData = (Double[]) request.requestData();

        return switch (requestType){
            case "add" -> new Response(ResponseCode.OK, reQuestData[0] + reQuestData[1]);
            case "minus" -> new Response(ResponseCode.OK, reQuestData[0] - reQuestData[1]);
            case "multiply" -> new Response(ResponseCode.OK,reQuestData[0] * reQuestData[1]);
            case "divide" -> new Response(ResponseCode.OK, reQuestData[0] / reQuestData[1]);
            default -> new Response(ResponseCode.WRONG_DATA, "");
        };
    }

}