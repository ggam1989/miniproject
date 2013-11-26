package db;

public class LogInfo {

	GameVo vo1;
	static String id;
	//GameDAO gd = new GameDAO();

	public LogInfo(GameVo vo) {
		vo1 = vo;
		id = vo.getId();
	}

	public static String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
