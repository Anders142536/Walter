package Walter;

//this is a static collection of constants that may be called anywhere in the project
public class Collection {

    /* TODO: Write a function initialize() that loads all values from a file.
        This is to make typos or changes in the texts in general easier to do. Otherwise there may be changes
        to this project, rendering it uncompilable and we could not change the respective value. :<
     */

    //role IDs on our server
    final public static long WALTER_ROLE_ID        = 391285765893783554L;
    final public static long BOT_ROLE_ID           = 0L;
    final public static long ADMIN_ROLE_ID         = 254264388209475584L;
    final public static long MEMBER_ROLE_ID        = 254264987294498817L;
    final public static long GUEST_ROLE_ID         = 401876564154777620L;
    final public static long ENGLISH_ROLE_ID       = 431236126443831308L;
    final public static long PIMMEL_ROLE_ID        = 305413276181987329L;
    final public static long OVERLORD_ROLE_ID      = 391257227975196673L;
    final public static long NETFLIX_ROLE_ID       = 397462377521610762L;
    final public static long NEWSBRINGER_ROLE_ID   = 430366734617280542L;
    final public static long EVERYONE_ROLE_ID      = 254263827237961729L;

    //guild and channel IDs on our server
    final public static long GUILD_ID              = 254263827237961729L;
    final public static long FOYER_CHANNEL_ID      = 402875839131156501L;
    final public static long CONFIG_CHANNEL_ID     = 403266783844237323L;
    final public static long ADMIN_CHANNEL_ID      = 391278094352121857L;
    final public static long FORTNITE_CHANNEL_ID   = 403903598586757130L;
    final public static long GENERAL_CHANNEL_ID    = 305420693653159938L;
    final public static long DROPZONE_CHANNEL_ID   = 391292051712638987L;
    final public static long NEWS_CHANNEL_ID       = 405462316470108160L;
    final public static long CINEMA_CHANNEL_ID     = 305417211508555776L;

    //Strings that are used all over the place
    public static String NO_VOICELOG_FOUND = "No file called voice.logs found.";
    public static String ERROR_NOT_YET_IMPLEMENTED = "(Error: Anders was to lazy too implement something here.";

    //Strings used by commands
    public static String NOT_ENOUGH_ARGUMENTS_ENGLISH = "I am utterly sorry, but I was not given enough arguments.";
    public static String TOO_MANY_ARGUMENTS_ENGLISH = "I am utterly sorry, but I was given too many arguments.";
    public static String NOT_YET_IMPLEMENTED = "Es tut mir Leid, doch dies ist noch nicht implementiert. Bitte melde dies <@!151010441043116032> damit er es implementieren kann.";
    public static String NOT_YET_IMPLEMENTED_ENGLISH = "I am utterly sorry, but this is not yet implemented. Please report to <@!151010441043116032> so he can implement this.";
    /*public static String KEYWORDS = "\n\nSchlüsselworte für diesen Command sind:```";
    public static String KEYWORDS_ENGLISH = "\n\nKeywords for this Command are:```";
    public static String CAUTION_CASEINSENSITIVE = "Achtung:\nDie Groß- und Kleinschreibung ist irrelevant.";
    public static String CAUTION_CASEINSENSITIVE_ENGLISH = "Caution:\nThese are not case-sensitive.";*/
    public static String CAUTION_QUOTES_FOR_WHITESPACE = ERROR_NOT_YET_IMPLEMENTED;
    public static String CAUTION_QUOTES_FOR_WHITESPACE_ENGLISH = ERROR_NOT_YET_IMPLEMENTED;



}
