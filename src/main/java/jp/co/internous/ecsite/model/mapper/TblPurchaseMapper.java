package jp.co.internous.ecsite.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jp.co.internous.ecsite.model.dto.HistoryDto;

@Mapper
public interface TblPurchaseMapper {
	List<HistoryDto> findHistory(int userId);
	int insert(int userId, int goodsId, String goodsName, int itemCount, int total);

}
//↑insert文が読まれた時点で一瞬xmlファイルも確認される（id=insertがないか　あればそこの変数がここのinsertに入れられる処理になる）