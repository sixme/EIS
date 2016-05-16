package nl.tudelft.goal.thesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;

import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;

/**
 * Entity received by 'MyAgent' which parses user's input and sends percepts back to the agent. The agent can interact with the user via the 'say' and
 * 'end' actions.
 * 
 * @author Santiago Conde
 */
public class Entity {

    private final String   newLine           = System.getProperty("line.separator");
    private final String[] knowBasic         = { "gender", "age" };
    private final String[] knowMore          = { "partner", "futureJob" };
    private List<String>   knowBasic_l;
    private List<String>   knowMore_l;

    private String         lastInput         = "";
    private String         currentID         = null;
    private String         userName          = "";
    private String         collaborative     = "";
    private String         value             = "";
    private String         what              = "";
    private final String   entity            = "user";

    private JTextArea      textArea;
    private JFrame         frame;
    private JTextField     input;

    private boolean        clarification     = false;
    // Booleans to control when to send the different percepts
    private boolean        sendHas           = false;
    private boolean        sendFailed        = false;
    private boolean        sendUserWantsToGo = false;
    private boolean        sendCollaborative = false;
    private boolean        sendIs            = false;
    private boolean        sendDislike       = false;
    private boolean        sendLike          = false;
    private boolean        sendWant          = false;
    private boolean        sendReason        = false;
    private boolean        partnerDone       = false;

    // Constructor
    public Entity(JFrame f) {
        this.frame = f;
        JPanel a = (JPanel) ((JPanel) (((JPanel) ((JLayeredPane) frame.getRootPane().getComponents()[1]).getComponents()[0]).getComponents()[0]))
                .getComponents()[1];
        this.input = (JTextField) a.getComponents()[2];
        JViewport b = (JViewport) ((JScrollPane) ((JPanel) (((JPanel) ((JLayeredPane) frame.getRootPane().getComponents()[1]).getComponents()[0])
                .getComponents()[0])).getComponents()[0]).getComponents()[0];
        this.textArea = (JTextArea) b.getComponents()[0];
        knowBasic_l = Arrays.asList(knowBasic);
        knowMore_l = Arrays.asList(knowMore);
        System.out.println("Entity created");

    }

    /**
     * Action to quit the interaction with the user displaying a goodbye message.
     * 
     * @param text
     *            - Message to be displayed
     */
    @AsAction(name = "end")
    public void kill(String text) {
        textArea.append(text + newLine);
        try {
            // Wait for the message to be displayed
            Thread.sleep(5000);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Action to say something to the user.
     * 
     * @param type
     *            - type of interaction: statement, ask or clarification.
     * @param ID
     *            - identification of the interaction (eg: gender, age...).
     * @param text
     *            - message to be printed.
     * @throws InterruptedException
     *             - when changing interactions (aka changing IDs) the conversation is suspended for some milis to increase the realism in the
     *             conversation, if the Thread is interrupted in that time it will throw an InterruptedException
     */
    @AsAction(name = "say")
    public void say(String type, String ID, String text) throws InterruptedException {
        // Welcome message
        if (currentID == null && ID != null) {
            currentID = ID;
            textArea.append(text + newLine);
            return;
        }
        // Stop sending failed after a clarification
        if (clarification && !type.equals("clarification")) {
            clarification = false;
            sendFailed = false;
        }

        /** Stop sending the percepts **/

        // Stop sending failed percept (normal)
        if (type.equals("clarification")) {
            sendFailed = false;
            clarification = true;
        }

        // Stop sending HAS percept
        if (sendHas) {
            sendHas = false;
            value = "";
        }

        // Stop sending REASON percept
        if (sendReason) {
            sendReason = false;
            what = "";
            value = "";
        }

        // Stop sending WANT percept
        if (sendWant) {
            sendWant = false;
            what = "";
            value = "";
        }

        // Stop sending DISLIKES percept
        if (sendDislike) {
            sendDislike = false;
            value = "";
        }

        // Stop sending DISLIKES percept
        if (sendLike) {
            sendLike = false;
            value = "";
        }
        // Stop sending collaborative
        if (sendCollaborative)
            sendCollaborative = false;
        // Stop sending IS
        if (sendIs)
            sendIs = false;

        // Changing the ID
        if (ID != null && !ID.equals(currentID)) {
            // small delay so it looks more natural on the shell
            Thread.sleep(300);
            currentID = ID;
            textArea.append(text + newLine);
            // Same ID
        } else {
            textArea.append(text + newLine);
        }
    }

    /**
     * IS percept
     * 
     * @return an arraylist containing the entity, the percept and the value (for instance IS(user, gender, a boy)).
     */
    @AsPercept(name = "iis", multipleArguments = true)
    public ArrayList<String> is() {
        ArrayList<String> per = new ArrayList<String>();
        if (sendIs) {
            per.add(entity);
            per.add(currentID);
            per.add(value);
            return per;
        } else
            return null;
    }

    /**
     * HAS percept
     * 
     * @return an arraylist containing the entity that has a percept and the value for that percept(for instance HAS(user, userName, 'name')).
     */
    @AsPercept(name = "has", multipleArguments = true)
    public ArrayList<String> has() {
        ArrayList<String> per = new ArrayList<String>();
        if (sendHas && !currentID.equals("") && !currentID.equals("''") && !currentID.equals("'")) {
            per.add(entity);
            per.add(currentID);
            per.add(value);
            return per;
        } else
            return null;
    }

    /**
     * Collaborative percept
     * 
     * @return a string indicating whether the user is collaborating or not with the agent.
     */
    @AsPercept(name = "collaborative")
    public String sendCollaborative() {
        if (sendCollaborative) {
            return collaborative;
        } else
            return null;
    }

    /**
     * Dislike percept
     * 
     * @return a string indicating what the user dislikes
     */
    @AsPercept(name = "dislikes")
    public String dislikes() {
        if (sendDislike) {
            return value;
        } else
            return null;
    }

    /**
     * Like percept
     * 
     * @return a string indicating what the user likes
     */
    @AsPercept(name = "likes")
    public String likes() {
        if (sendLike) {
            return value;
        } else
            return null;
    }

    /**
     * Reason percept
     * 
     * @return a string indicating the reason of something
     */
    @AsPercept(name = "reason", multipleArguments = true)
    public ArrayList<String> reason() {
        ArrayList<String> per = new ArrayList<String>();
        if (sendReason) {
            per.add(entity);
            per.add(what);
            per.add(value);
            return per;
        } else
            return null;
    }

    /**
     * Reason percept
     * 
     * @return a string indicating the reason of something
     */
    @AsPercept(name = "wants", multipleArguments = true)
    public ArrayList<String> want() {
        ArrayList<String> per = new ArrayList<String>();
        if (sendWant) {
            per.add(entity);
            per.add(what);
            per.add(value);
            return per;
        } else
            return null;
    }

    /**
     * Failed percept, used to indicate that there was a problem in the parsing.
     * 
     * @return a string containing a 'f'.
     */
    @AsPercept(name = "failed")
    public String failed() {
        if (sendFailed)
            return "f";
        else
            return null;
    }

    // Main loop for processing inputs, for every input it calls the method processInput.
    @AsPercept(name = "a")
    public String getUserInput() {
        String text = input.getText();
        if (!text.equals("") && !text.equals(lastInput)) {
            lastInput = text;
            appendUser(text);
            processInput(text);
            return null;
        }
        return null;

    }

    // Method which actually processes the inputs
    private void processInput(String text) {
        if (currentID != null) {
            if (text != null && !text.equals("") && !text.equals(lastInput)) {
                lastInput = text;
                appendUser(text);
            }
            // USER WANTS TO GO
            if (text.trim().equals("I want to go") || text.trim().equals("I am sorry but I have to leave now")) {
                sendUserWantsToGo = true;
                sendUserWantsToGo();
                return;
            }
            // NAME
            if (currentID.equals("userName")) {
                if (text.toLowerCase().startsWith("my name is ")) {
                    userName = text.toLowerCase().split("my name is ")[1].trim();
                    sendHas = true;
                    value = userName.substring(0, 1).toUpperCase() + userName.substring(1);
                    userName = value;
                    has();
                    collaborative();
                } else if (countWords(text.toLowerCase()) == 1) {
                    userName = text.substring(0, 1).trim().toUpperCase() + text.trim().substring(1);
                    value = userName;
                    sendHas = true;
                    has();
                } else if (text.trim().toLowerCase().equals("i don't want to tell you my name") || text.trim().toLowerCase()
                        .equals("no, i don't want")) {
                    notCollaborative();
                } else {
                    sendFailed = true;
                    failed();
                }
                // KNOW BASIC USER
            } else if (knowBasic_l.contains(currentID)) {
                if (text.startsWith("I am ")) {
                    value = text.split("I am ")[1].trim();
                    sendIs = true;
                    is();
                    collaborative();
                } else if (text.matches("^[0-9]*$") && currentID.equals("age")) { // number with the age
                    value = text;
                    sendIs = true;
                    is();
                    collaborative();
                } else if (text.trim().equals("No") || text.trim().equals("I don't want")) {
                    notCollaborative();
                } else if (clarification) {
                    if (text.trim().toLowerCase().startsWith("yes")) {
                        value = "a boy";
                        sendIs = true;
                        is();
                        collaborative();
                    } else if (text.trim().toLowerCase().startsWith("no") || text.trim().toLowerCase().contains("girl")) {
                        value = "a girl";
                        sendIs = true;
                        is();
                        collaborative();
                    } else {
                        sendFailed = true;
                        failed();
                    }
                } else {
                    sendFailed = true;
                    failed();
                }
                // KNOW MORE
            } else if (knowMore_l.contains(currentID)) {
                // DISLIKE TOPIC
                if (text.toLowerCase().contains("don't want to") || text.toLowerCase().contains("don't like to")) {
                    notCollaborative();
                }
                if (currentID.equals("partner")) { // PARTNER
                    if ((countWords(text.trim()) == 1 || countWords(text.trim()) == 2) && !text.toLowerCase().contains("yes") && !partnerDone) {
                        value = text.substring(0, 1).trim().toUpperCase() + text.trim().substring(1);
                        what = "partner";
                        sendHas = true;
                        partnerDone = true;
                        has();
                        collaborative();
                    } else if (text.trim().toLowerCase().contains("yes")) {
                        value = what;
                        sendLike = true;
                        likes();
                    } else if (text.trim().toLowerCase().equals("no")) {
                        value = "partner";
                        sendDislike = true;
                        dislikes();
                    } else if (text.toLowerCase().contains("don't have")) {
                        value = "no";
                        what = "being single";
                        sendHas = true;
                        has();
                        collaborative();
                    } else if (text.toLowerCase().contains("would like") || text.toLowerCase().contains("want a")) {
                        value = "being single";
                        what = value;
                        sendDislike = true;
                        dislikes();
                    } else if (text.toLowerCase().contains("because")) {
                        value = text.toLowerCase().substring(text.toLowerCase().indexOf("because"));
                        sendReason = true;
                        reason();
                    } else {
                        sendFailed = true;
                        failed();
                    }
                } else if (currentID.equals("futureJob")) {
                    if (text.toLowerCase().trim().contains("i want to be ")) {
                        value = text.substring(text.indexOf("i want to be ") + 14);
                        what = "futureJob";
                        sendWant = true;
                        want();
                    } else {
                        sendFailed = true;
                        failed();
                    }
                } // JOKE
            } else if (currentID.equals("joke")) {
                if (text.trim().toLowerCase().contains("yes") || text.trim().toLowerCase().contains("haha")) {
                    collaborative();
                } else if (text.trim().toLowerCase().contains("no") || text.trim().toLowerCase().contains("boring") || text.trim().toLowerCase()
                        .contains("don't like")) {
                    notCollaborative();
                } else {
                    sendFailed = true;
                    failed();
                }
            }
        }
    }

    /**
     * A percept used to indicate whether user wants to quit the interaction.
     * 
     * @return a string with 'go'.
     */
    @AsPercept(name = "userWantsToGo")
    public String sendUserWantsToGo() {
        if (sendUserWantsToGo) {
            return "go";
        } else
            return null;
    }

    // credit: http://stackoverflow.com/a/5864174
    public int countWords(String string) {
        String trim = string.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length; // separate string around spaces
    }

    public void appendUser(String text) {
        textArea.append("[You]->" + text + System.getProperty("line.separator"));
    }

    private void notCollaborative() {
        collaborative = "Negative";
        sendCollaborative = true;
        sendCollaborative();
    }

    private void collaborative() {
        collaborative = "Positive";
        sendCollaborative = true;
        sendCollaborative();
    }

}
