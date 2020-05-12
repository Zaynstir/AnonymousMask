package io.github.zaynstir.anonymous;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class SaveTeam {
    private Player player;
    private Team team;

    public SaveTeam(){
        player = null;
        team = null;
    }

    public SaveTeam(Player player, Team team){
        this.player = player;
        this.team = team;
    }

    public Player getCurrentPlayer(){
        return player;
    }

    public Team getCurrentTeam(){
        return team;
    }

    public void setCurrentPlayer(Player player){
        this.player = player;
    }

    public void setCurrentTeam(Team team){
        this.team = team;
    }
}
