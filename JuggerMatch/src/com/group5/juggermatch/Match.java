package.com.group5.juggermatch;

public class Match {
	
	long _id;						//match id
	private String team_1;          // team1 name
	private String team_2;          //team2 name
	private int score_team_1;    //score of the first team
	private int score_team_2;    //score of the second team
	private long start_time;                 // start time of the match
	private long end_time;                   // end time of the match
	private String location;


	public long getId() {
    	return this._id; 	    	
    }
	
    public void setId(long id){
    	this._id = id;
    }

    public String getTeam1() {
		return team_1;
	}

	public void setTeam1(String t_1) {
		this.team_1 = t_1;
	}

	public String getTeam2() {
		return team_2;
	}

	public void setTeam2(String t_2) {
		this.team_2 = t_2;
	}

	public int getScoreTeam1() {
		return score_team_1;
	}

	public void setScoreTeam1(int s_t_1) {
		this.score_team_1 = s_t_1;
	}  
    
	public int getScoreTeam2() {
		return score_team_2;
	}

	public void setScoreTeam2(int s_t_2) {
		this.score_team_2 = s_t_2;
	}  
	
	public long getStartTime() {
		return this.start_time;		
	}
	
	public void setStartTime(long st_t) {
		this.start_time = st_t;
	}
	
	public long getEndTime() {
		return this.end_time;		
	}
	
	public void setEndTime(long end_t) {
		this.end_time = end_t;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String loc) {
		this.location = loc;
	}
}	
