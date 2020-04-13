package Walter.enums;

public enum BlackCategory {
    ADMIN                       (402882380953681922L),
    CATSANDMEMES                (361165294590230529L),
    MEOWANDPURRPURR             (402885052687581215L),
    DONOTDISTURB                (361166492378726415L),
    TURRETDEFENSE               (646455228488482846L),
    GAMEANDCATTOYDISCUSSIONS    (361166928489873408L),
    UNUSEDGAMECHANNELS          (533049033720070154L),
    ARCHIV                      (413314004660912128L);

    public final long ID;

    BlackCategory(long ID) { this.ID = ID; }
}
