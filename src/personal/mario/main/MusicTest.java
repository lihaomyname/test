package personal.mario.main;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import personal.mario.bean.MusicCommentMessage;
import personal.mario.service.TopMusicCalculateService;
import personal.mario.utils.GenerateExcelUtils;

public class MusicTest {
	
	private static HSSFWorkbook commentMessageWorkbook = new HSSFWorkbook();
	public static void main(String[] args) throws IOException {
		
		HSSFSheet commentMessageSheet = GenerateExcelUtils.generateCommentMessageExcelInit(commentMessageWorkbook);
		NetEaseCrawler net = new NetEaseCrawler();
		MusicCommentMessage mcm =net.getCommentMessage("63692");
		//判断是否加入Top歌曲列表
		List<MusicCommentMessage> ms = TopMusicCalculateService.getTopMusic(mcm);
		
		//歌曲评论数是否大于某个值进行收录
		List<MusicCommentMessage> msl = TopMusicCalculateService.getMusicCommentsCountMore(mcm);
		
		//向歌曲信息Excel插入数据
		int commentCount = mcm.getCommentCount();
		System.out.println(commentCount+"...................................................");
		GenerateExcelUtils.generateCommentMessageExcelProcess(commentMessageWorkbook, commentMessageSheet, mcm, 200);
		
		//生成歌曲评论Excel
		GenerateExcelUtils.generateCommentsExcel(mcm);
	}
}	
