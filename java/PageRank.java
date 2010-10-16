import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class PageRank {

	public static Map rank = new HashMap();
	public static Map followerMap = new HashMap();
	public static Map friendMap = new HashMap();
	public static double threshold = 0.00000001;

	public static String dirStr = "D:\\JHU\\BDSLSS\\Project\\Iteration 0\\MyGraph";

	public static double d = .85;
	
	public static int categories = 2;
	public static int queryCategory = 0;
	public static Map[] matrix = new Map[categories];
	
	
	
	public static String[] getFollowers(String id) {
		if (!followerMap.containsKey(id)) {
			File followerFile = new File(dirStr + "\\" + id + ".followers");
			if (!followerFile.exists()) {
				return null;
			}
			
			String text = null;
			try {
				BufferedReader reader = new BufferedReader(new FileReader(followerFile));
				text = reader.readLine();
				reader.close();							
			}catch (Exception e) {
				System.err.println(e.getMessage());
			}
			if (text == null) {
				return null;
			}
			text = text.substring(1, text.length() - 1);
			
			List followerList = new ArrayList();
			String[] splitList = text.split(",");
			for (int i = 0; i < splitList.length; i++) {
				followerList.add(splitList[i]);
			}
			followerMap.put((String)id, followerList);
			
			return splitList;
		} else {
			Object[] followerList = ((List)followerMap.get(id)).toArray();
			String[] followerListStr = new String[followerList.length];
			for (int i = 0; i < followerList.length; i++) {
				followerListStr[i] = String.valueOf(followerList[i]);
			}
			return followerListStr;
		}
	}
	
	
	public static String[] getFriends(String id) {
		if (!friendMap.containsKey(id)) {
			File friendFile = new File(dirStr + "\\" + id + ".friends");
			if (!friendFile.exists()) {
				return null;
			}
			
			String text = null;
			try {
				BufferedReader reader = new BufferedReader(new FileReader(friendFile));
				text = reader.readLine();
				reader.close();							
			}catch (Exception e) {
				System.err.println(e.getMessage());
			}
			if (text == null) {
				return null;
			}
			text = text.substring(1, text.length() - 1);
			
			List friendList = new ArrayList();
			String[] splitList = text.split(",");
			for (int i = 0; i < splitList.length; i++) {
				friendList.add(splitList[i]);
			}
			friendMap.put((String)id, friendList);
			
			return splitList;
		} else {
			Object[] friendList = ((List)friendMap.get(id)).toArray();
			String[] friendListStr = new String[friendList.length];
			for (int i = 0; i < friendList.length; i++) {
				friendListStr[i] = String.valueOf(friendList[i]);
			}
			return friendListStr;
		}
	}
	
	public static double pageRank(String id) {
		double value = 0;
		
		String[] followers = getFollowers(id);

		if (followers == null) {
			return 1 - d;
		}
		
		for (String follower : followers) {
			String[] friends = getFriends(follower);
			double friendNum = 0;
			if (friends == null) {
				friendNum = 1;
			} else {
				if (friends.length == 0) {
					friendNum = 1;
				}else {
					//friendNum = friends.length;
					double sum = 0;
					double percentage = 0;
					for (String friendId : friends) {
						if (matrix[queryCategory].containsKey((String)friendId)) {
							sum += Double.valueOf((String)matrix[queryCategory].get(friendId));
						}
					}
					
					if (matrix[queryCategory].containsKey((String)id)) {
						percentage = Double.valueOf((String)matrix[queryCategory].get(id));
					}
					
					if (sum == 0) {
						sum = 1;
					}
					if (percentage == 0) {
						percentage = 1;
					}
					friendNum = percentage / sum;
				}
			}
			String followerRankStr = (String)rank.get((String)follower);
			double followerRank = 0;
			if (followerRankStr == null) {
				followerRank = 1 - d;
			}else {
				followerRank = Double.valueOf(followerRankStr);
			}
			
			
			value += followerRank * friendNum;
		}
		
		int n = followers.length;
		if (n == 0) {
			n = 1;
		}
		
		/*if (matrix[queryCategory].containsKey((String)id)) {
			double percentage = Double.valueOf((String)matrix[queryCategory].get(id));
			value *= percentage;
		}*/
		
		value = (1 - d) / (double)n + d * value;
		return value;
	}
	
	public static void graphUpdate(String from, String to) {

		// Graph update 7 -> 2
		
		List updateList = new ArrayList();
		if (!rank.containsKey(to)) {
			rank.put(to, String.valueOf("1.0"));
		}
		if (!rank.containsKey(from)) {
			rank.put(from, "1.0");
		}
		
		List followerList = new ArrayList();
		if (followerMap.containsKey(to)) {
			followerList = (List)followerMap.get(to);
		}
		followerList.add(from);
		followerMap.put(to, followerList);
		
		List friendList = new ArrayList();
		if (friendMap.containsKey(from)) {
			friendList = (List)friendMap.get(from);
		}		
		friendList.add(to);
		friendMap.put(from, friendList);
		
		
		
		updateList.add(from);
		updateList.add(to);

		int testCount = 0;
		do {
			Object[] formerUpdate = updateList.toArray();
			for (int i = 0; i < formerUpdate.length; i ++) {
				String key = formerUpdate[i].toString();
				
				double currentValue = Double.valueOf((String)rank.get(key));
				double nextValue = pageRank(key);
				
				System.out.println(key + " : currentValue : " + currentValue + "    nextValue : " + nextValue);
				if (Math.abs(currentValue - nextValue) <= threshold) {
					updateList.remove(key);
				} else {
					rank.put(key, String.valueOf(nextValue));
					String[] friends = getFriends(key);
					if (friends != null) {
						for (String friend : friends) {
							updateList.add(friend);
						}
					}
				}
			}
			testCount++;
		}while (updateList.size() > 0);
		System.out.println(testCount);
	}
	
	public static void main(String[] args) {
		matrix[0] = new HashMap();
		matrix[1] = new HashMap();
		matrix[0].put("0", ".5");
		matrix[1].put("0", ".5");
		matrix[0].put("1", ".9");
		matrix[1].put("1", ".1");
		matrix[0].put("2", ".05");
		matrix[1].put("2", ".85");
		matrix[0].put("3", ".5");
		matrix[1].put("3", ".5");
		matrix[0].put("4", ".8");
		matrix[1].put("4", ".2");
		matrix[0].put("5", ".5");
		matrix[1].put("5", ".5");
		matrix[0].put("6", ".5");
		matrix[1].put("6", ".5");
		
		
		
		
		
		File dir = new File(dirStr);
		File file[] = dir.listFiles();
		
		for (File tempFile : file) {
			String fileName = tempFile.getName();
			String[] seperate = fileName.split("\\.");
			if (seperate[1].equals("followers") || seperate[1].equals("friends")) {
				rank.put((String)seperate[0], "1.0");
			}
		}
		
		int count = 0;
		double gap = 0;
		do {
			gap = 0;
			Iterator ite = rank.keySet().iterator();
			Map rankNext = new HashMap();
			while (ite.hasNext()) {
				String key = (String)ite.next();
				double nextValue = pageRank(key);
				rankNext.put((String)key, String.valueOf(nextValue));
				
				String currentValue = (String)rank.get(key);
				gap += Math.abs(Double.valueOf(currentValue) - nextValue);
			}
			
			rank = rankNext;
			
			System.out.println("\n");
			
			Iterator iteOutput = rank.keySet().iterator();
			while (iteOutput.hasNext()) {
				String key = (String)iteOutput.next();
				System.out.println(count + ": Rank of " + key + " is : " + rank.get(key));
			}
			count++;
			System.out.println("gap " + gap);
		}while (gap > threshold * rank.size());
				
		//graphUpdate("6", "4");
		

		
		String[] resultKey = new String[rank.size()];
		double[] resultValue = new double[rank.size()];
		Iterator iteOutput = rank.keySet().iterator();
		int countResult = 0;
		while (iteOutput.hasNext()) {
			String key = (String)iteOutput.next();
			double value = Double.valueOf((String)rank.get(key));
			int i = 0;
			for (i = 0; i < countResult; i++) {
				if (value > resultValue[i]) {
					break;
				}
			}
			for (int j = rank.size() - 1; j > i; j--) {
				resultKey[j] = resultKey[j - 1];
				resultValue[j] = resultValue[j - 1];
			}
			resultKey[i] = key;
			resultValue[i] = value;
			countResult ++;
		}
		
		System.out.println();
		for (int i = 0; i < rank.size(); i++) {
			System.out.println("Rank of " + resultKey[i] + " is : " + resultValue[i]);
		}
	}

}
