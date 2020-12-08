package com.tomtom.pravin.shellConsole.upwork;

import com.google.gson.Gson;

import java.time.LocalTime;
import java.util.*;

public class PushNotification extends Thread {

    private static final Map<String, String> templates = new HashMap<>();
    private static final List<String> input = new ArrayList<>();

    static {
        templates.put("template1", "Hi %s, \t\t\t\t%s\n" +
                "I hope your phone no. is still the same.\n" +
                "Talk to you soon.\n" +
                "%s");
        templates.put("template2", "Hi %s, \t\t\t\t%s\n" +
                "This is a second message, i am trying to reach you.\n" +
                "Talk to you soon.\n" +
                "%s");
        templates.put("template3", "Hi %s, \t\t\t\t%s\n" +
                "This is a third message, i am trying to reach you.\n" +
                "Talk to you soon.\n" +
                "%s");
        templates.put("template4", "Hi %s, \t\t\t\t%s\n" +
                "This is a fourth message, i am trying to reach you.\n" +
                "Talk to you soon.\n" +
                "%s");
        input.add("{\n" +
                "    \"action\": \"sendMessage\",\n" +
                "    \"senderName\": \"EmployeeA\",\n" +
                "    \"customerName\": \"John Doe\",\n" +
                "    \"customerId\": 1232,\n" +
                "    \"initialDelay\": 5,\n" +
                "    \"email\": \"johndoe@gmail.com\",\n" +
                "    \"templateId\": \"template1\"\n" +
                "}");
        input.add("{\n" +
                "    \"action\": \"sendMessage\",\n" +
                "    \"senderName\": \"EmployeeA\",\n" +
                "    \"customerName\": \"John Doe\",\n" +
                "    \"customerId\": 1232,\n" +
                "    \"initialDelay\": 10,\n" +
                "    \"email\": \"johndoe@gmail.com\",\n" +
                "    \"templateId\": \"template2\"\n" +
                "}");
        input.add("{\n" +
                "    \"action\": \"sendMessage\",\n" +
                "    \"senderName\": \"EmployeeA\",\n" +
                "    \"customerName\": \"John Doe\",\n" +
                "    \"customerId\": 1232,\n" +
                "    \"initialDelay\": 15,\n" +
                "    \"email\": \"johndoe@gmail.com\",\n" +
                "    \"templateId\": \"template3\"\n" +
                "}");
        input.add("{\n" +
                "    \"action\": \"sendMessage\",\n" +
                "    \"senderName\": \"EmployeeA\",\n" +
                "    \"customerName\": \"John Doe\",\n" +
                "    \"customerId\": 1232,\n" +
                "    \"initialDelay\": 20,\n" +
                "    \"email\": \"johndoe@gmail.com\",\n" +
                "    \"templateId\": \"template4\"\n" +
                "}");
    }

    public void run() {
        int timeTaken = 0;
        try {
            for (String currentCommand : input) {
                Request request = parseRequest(currentCommand);
                //additional null checks here
                Thread.sleep((request.getInitialDelay() - timeTaken) * 1000);
//               Thread.sleep((request.getInitialDelay() - timeTaken)*60* 1000);
                timeTaken = request.getInitialDelay();
                executeAndDispatch(request);
            }
        } catch (InterruptedException e) {
            System.out.println("Stopped sending further messages to customer");
        }
    }

    private void executeAndDispatch(Request request) {
        String content = String.format(templates.get(request.getTemplateId()), request.getCustomerName(), LocalTime.now().toString(), request.getSenderName());
        System.out.println("Sending Email : " + request.getEmail() + "\n" + content + "\n");
    }

    public static void main(String[] args) {
        //right now the timer is set to seconds not on minutes
        PushNotification pushNotificationThread = new PushNotification();
        pushNotificationThread.start();
        Scanner scanner = new Scanner(System.in);
        String customerResponse = scanner.nextLine();
        if (!customerResponse.isEmpty()) {
            pushNotificationThread.interrupt();
            System.out.println("Customer Response : \n" + customerResponse);
        }
    }

    private static Request parseRequest(String input) {
        try {
            final Gson gson = new Gson();
            Request request = gson.fromJson(input, Request.class);
            validateRequestBody(request);
            return request;
        } catch (Exception e) {
            System.out.println("Failed Parsing the Command");
        }
        return null;
    }

    private static void validateRequestBody(Request request) throws Exception {
        if (null == request.getAction() || null == request.getSenderName() || (null == request.getCustomerName() && 0 == request.getCustomerId())
                || null == request.getEmail() || null == request.getTemplateId() || 0 == request.getInitialDelay()) {
            throw new Exception("Invalid Request Body");
        }
    }

    class Request {
        private String action;
        private String senderName;
        private String customerName;
        private int customerId;
        private int initialDelay;
        private String email;
        private String templateId;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public int getInitialDelay() {
            return initialDelay;
        }

        public void setInitialDelay(int initialDelay) {
            this.initialDelay = initialDelay;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTemplateId() {
            return templateId;
        }

        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }
    }
}
