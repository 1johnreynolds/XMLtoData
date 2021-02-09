import java.util.Scanner;

public class AnswerQuery {
    private String chosenQuery;
    private String queryA_AuthorName;
    private String queryB_PaperName;
    private String queryC_JournalName;
    private String queryC_Year;
    private String queryC_Issue;
    private String queryD_ConferenceName;
    private String queryD_Year;

    public String chosen() {
        return chosenQuery;
    }

    public String returnQueryA_AuthorName() {
        return queryA_AuthorName;
    }

    public String returnQueryB_PaperName() {
        return queryB_PaperName;
    }

    public String returnQueryC_JournalName() {
        return queryC_JournalName;
    }

    public String returnQueryC_Year() {
        return queryC_Year;
    }

    public String returnQueryC_Issue() {
        return queryC_Issue;
    }

    public String returnQueryD_ConferenceName() {
        return queryD_ConferenceName;
    }

    public String returnQueryD_Year() {
        return queryD_Year;
    }

    private void answerQueryA() {
        chosenQuery = "a";

        Scanner qaAuthorName = new Scanner(System.in);
        System.out.print("Please tell me the author name: ");
        String authorName = qaAuthorName.nextLine();
        System.out.println("Given author name is: " + authorName);

        queryA_AuthorName = authorName;
//        returnQueryA_AuthorName();
    }

    private void answerQueryB() {
        chosenQuery = "b";
        Scanner qbPaperName = new Scanner(System.in);
        System.out.print("Please tell me the paper name: ");
        String paperName = qbPaperName.nextLine();
        System.out.println("Given paper name is: " + paperName);

        queryB_PaperName = paperName;
    }

    private void answerQueryC() {
        chosenQuery = "c";
        Scanner qcJournalName = new Scanner(System.in);
        System.out.print("Please tell me the journal name: ");
        String journalName = qcJournalName.nextLine();
        System.out.println("Given journal name is: " + journalName);

        Scanner qcYear = new Scanner(System.in);
        System.out.print("Please tell me the year: ");
        String year = qcYear.nextLine();
        System.out.println("Given year is: " + year);

        Scanner qcIssue = new Scanner(System.in);
        System.out.print("Please tell me the issue: ");
        String issue = qcIssue.nextLine();
        System.out.println("Given issue is: " + issue);

        queryC_JournalName = journalName;
        queryC_Year = year;
        queryC_Issue = issue;
    }

    private void answerQueryD() {
        chosenQuery = "d";
        Scanner qdConferenceName = new Scanner(System.in);
        System.out.print("Please tell me the conference name: ");
        String conferenceName = qdConferenceName.nextLine();
        System.out.println("Given conference name is: " + conferenceName);

        Scanner qdYear = new Scanner(System.in);
        System.out.print("Please tell me the year: ");
        String year = qdYear.nextLine();
        System.out.println("Given year is: " + year);

        queryD_ConferenceName = conferenceName;
        queryD_Year = year;
    }

    private void answerQueryEx() {
        chosenQuery = "ex";
        System.out.println("Query a: ");
        answerQueryA();
        System.out.println();

        System.out.println("Query b: ");
        answerQueryB();
        System.out.println();

        System.out.println("Query c: ");
        answerQueryC();
        System.out.println();

        System.out.println("Query d: ");
        answerQueryD();
        System.out.println();
    }

    public AnswerQuery() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Which query do you want to choose?");
        System.out.println("1.3.1 Given author name A, list all of her co-authors\n" +
                "1.3.2 Given a paper name, list its publication metadata\n" +
                "1.3.3 Given a journal name, a year (volume) and an issue (number), find out the metadata of all the papers published in the book\n" +
                "1.4.3 Given a conference name and a year, find out the metadata of all the papers published in the book");
        System.out.println("Please choose: ");
        System.out.println("a. 1.3.1\n" +
                "b. 1.3.2\n" +
                "c. 1.3.3\n" +
                "d. 1.3.4\n" +
                "ex. do query a to d in turn");

        String chosen = sc.nextLine();
        System.out.println("You have chosen query " + chosen);
        if (chosen.equals("a")) {
            answerQueryA();
        } else if (chosen.equals("b")) {
            answerQueryB();
        } else if (chosen.equals("c")) {
            answerQueryC();
        } else if (chosen.equals("d")) {
            answerQueryD();
        } else if (chosen.equals("ex")) {
            answerQueryEx();
        } else {
            System.out.println("Sorry, this query is not applicable");
        }
    }
}

