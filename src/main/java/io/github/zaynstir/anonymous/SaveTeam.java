package io.github.zaynstir.anonymous;

import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class SaveTeam {
    private UUID player;
    private Team team;

    public SaveTeam(){
        player = null;
        team = null;
    }

    public SaveTeam(UUID player, Team team){
        this.player = player;
        this.team = team;
    }

    public UUID getCurrentPlayer(){
        return player;
    }

    public Team getCurrentTeam(){
        return team;
    }

    public void setCurrentPlayer(UUID player){
        this.player = player;
    }

    public void setCurrentTeam(Team team){
        this.team = team;
    }
}
