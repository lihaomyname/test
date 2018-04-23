package personal.mario.main;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import personal.mario.bean.MusicCommentMessage;
import personal.mario.service.MusicListQueueService;
import personal.mario.service.MusicQueueService;
import personal.mario.service.TopMusicCalculateService;
import personal.mario.utils.GenerateExcelUtils;

public class MusciTest {
	private static HSSFWorkbook commentMessageWorkbook = new HSSFWorkbook();
	public static void main(String[] args) throws IOException {
		HSSFSheet commentMessageSheet = GenerateExcelUtils.generateCommentMessageExcelInit(commentMessageWorkbook);
		NetEaseCrawler net = new NetEaseCrawler();
		int count =0;
		//开始根据歌单爬取
		while (!MusicListQueueService.isUncrawledMusicListEmpty()) {
			
			//填充待爬取歌曲队列
			net.fillUncrawledMusicQueue(MusicListQueueService.getTopMusicList());
			
			//歌曲队列为空就返回上层循环填充歌曲队列
			while (!MusicQueueService.isUncrawledMusicQueueEmpty()) {
				
				//取出待爬取歌曲ID
				String songId = MusicQueueService.getTopMusicUrl();
				
				//判断是否已经爬取过
				if (!MusicQueueService.isMusicCrawled("445665094")) {
					//获取到爬取结果，歌曲信息
					MusicCommentMessage mcm = net.getCommentMessage("445665094");
					
					//判断是否加入Top歌曲列表
					List<MusicCommentMessage> ms  = TopMusicCalculateService.getTopMusic(mcm);
					
					//歌曲评论数是否大于某个值进行收录
					List<MusicCommentMessage> msl = TopMusicCalculateService.getMusicCommentsCountMore(mcm);
					
					//向歌曲信息Excel插入数据
					GenerateExcelUtils.generateCommentMessageExcelProcess(commentMessageWorkbook, commentMessageSheet, mcm, count);
					
					//生成歌曲评论Excel
					GenerateExcelUtils.generateCommentsExcel(mcm);
					
					//加入已经爬取的队列，供以后查重判断
					MusicQueueService.addCrawledMusic(songId);
					count++;
				}
			}
		}
	}
}
