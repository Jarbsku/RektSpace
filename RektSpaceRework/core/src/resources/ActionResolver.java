package resources;

/**
 * Created by Jarbsku on 22.3.2015.
 */
public interface ActionResolver {

    public boolean getSignedInGPGS();
    public void loginGPGS();
    public void submitScoreGPGS(int score);
    public void unlockAchievementGPGS(String achievementId);
    public void getLeaderboardGPGS();
    public void getAchievementsGPGS();

}
