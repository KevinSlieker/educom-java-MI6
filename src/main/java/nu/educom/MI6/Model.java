package nu.educom.MI6;

import java.time.Duration;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.sql.SQLException;

public class Model {
//    private static String CodeSentence = "For ThE Royal QUEEN";
    private static ArrayList<String> blacklist = new ArrayList<String>();

    private List<LoginAttempt>      loginAttempts   = new ArrayList<LoginAttempt>();



    private Map<String, String> errors = new HashMap<String, String>();
    private Crud crud;
    private Agent agent;
    private View view;
    private int result;

    private  IPresenter presenter;

    public Map<String, String> getErrors() {
        return errors;
    }
    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public Model(Crud crud, View view) {
        this.crud = crud;
        this.view = view;
    }

    public void validateAgentNumber(String agentNumber){
        boolean check = true;
        errors.clear();
        try{
            Integer.parseInt(agentNumber);
            //if (agentNumber == null) {System.exit(0);}
        } catch (Exception e) {
            errors.put("Validation", "Only numbers are allowed.");
            check = false;
            return;
        }

        int lengInput = agentNumber.length();
        if (lengInput > 3) {
            errors.put("Validation", String.format("Agent number %s is wrong", agentNumber));
            agentNumber =  "0".repeat(3 - lengInput) + agentNumber;
            crud.createLoginRow(agentNumber, false);
            check = false;
        }
        if (Integer.parseInt(agentNumber) < 1 || Integer.parseInt(agentNumber) > 956) {
            errors.put("Validation", String.format("Agent number %s is not in defined range.", agentNumber));
            agentNumber =  "0".repeat(3 - lengInput) + agentNumber;
            crud.createLoginRow(agentNumber, false);
            check = false;
        }

        if (lengInput < 3) {
            agentNumber =  "0".repeat(3 - lengInput) + agentNumber;
            //System.out.println(agentNumber);
        }
        if (check == true) {
            this.agent = new Agent();
            agent.setAgentNumber(agentNumber);
        }
    }

    public Boolean authenticateAgent(String agentNumber) throws SQLException {
//        var check2 = 0;
//        errors.clear();
        agent = crud.readOneAgentRow(agentNumber);
        if (agent==null){
            errors.put("Validation", "Access Denied");
//            check2 += 1;
            return false;
        }
        this.loginAttempts = crud.getLastLoginAttempts(agent.getAgentNumber());
        if (!agent.getPersonal_sentence().equals(view.getSentence())) {
            errors.put("Validation",String.format("Access Denied %s", agent.getAgentNumber()));
//            check2 += 1;
        }
        if (!agent.isActive()==true) {errors.put("Validation",String.format("Access Denied, you are inactive %s", agent.getAgentNumber()));
//            check2 += 1;
//            return false;
        }
        if (!(checkTimeOut()==0)) {errors.put("Validation", String.format("You are time out for %s more seconds %s", result ,agent.getAgentNumber()));
            return false;}
        if (checkTimeOut()==0) {crud.createLoginRow(agent.getAgentNumber(), errors.isEmpty());}
//        return (check2==0);
        return errors.isEmpty();

    }

    private int checkTimeOut() {
        int tries = 0;
        try {tries = this.loginAttempts.size();}
        catch (Exception e) {}

        if (tries > 0) {
            var now     = LocalDateTime.now();
            var lastAttempt     = this.loginAttempts.get(tries-1).getDate();
            int diff    = (int) Duration.between(lastAttempt, now).getSeconds();
            int timeout = (int) (Math.pow(2, tries-1)*60);
            result  = timeout-diff;
            return Math.max(result, 0);
        }
        return 0;
    }


    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public List<LoginAttempt> getLoginAttempts() {
        return loginAttempts;
    }
}

