package gridworld;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QReadMaze {
	public QCell[][] maze = new QCell[6][6];
	public QCell startCell;
	
	public QReadMaze(String filePath, double nonTerminalReward) {        
        try {
	        FileReader f = new FileReader(filePath);
	        BufferedReader b = new BufferedReader(f);
	        Pattern pattern = Pattern.compile("(\\w|[\\+|\\-]\\d+|\\%)");
	        Matcher matcher;
	        String s = null;
	        String tmp = null;
	        int j = 0;
	        int i = 0;
	        while ((s = b.readLine()) != null) {
	        	matcher = pattern.matcher(s);
	        	while (matcher.find()) {
	        		QCell c = new QCell();
	        		tmp = matcher.group();
	        		if (tmp.contentEquals("s")) {
	        			startCell = c;
	        			c.reward = nonTerminalReward;
	        		} else if (tmp.contentEquals("b")) {
	        			c.reward = nonTerminalReward;
	        		} else if (tmp.contentEquals("%")) {
	        			c.wall = true;
	        		} else {
	        			if (tmp.charAt(0) == '+') {
	        				tmp = tmp.substring(1);
	        			}
	        			int n = Integer.parseInt(tmp);
	        			c.reward = n;
	        			c.utility = n;
	        			c.goal = true;
	        		}
	        		c.row = i;
	        		c.col = j;
	        		maze[i][j] = c;
	        		j++;
	        		j %= 6;
	        	}
	        	i++;
        		i %= 6;
	        }
	        f.close();
        }
		catch (Exception ex){
			ex.printStackTrace();
	    }
    }
}
