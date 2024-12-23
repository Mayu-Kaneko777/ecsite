package jp.co.internous.ecsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.ecsite.model.domain.MstGoods;
import jp.co.internous.ecsite.model.domain.MstUser;
import jp.co.internous.ecsite.model.dto.HistoryDto;
import jp.co.internous.ecsite.model.form.CartForm;
import jp.co.internous.ecsite.model.form.HistoryForm;
import jp.co.internous.ecsite.model.form.LoginForm;
import jp.co.internous.ecsite.model.mapper.MstGoodsMapper;
import jp.co.internous.ecsite.model.mapper.MstUserMapper;
import jp.co.internous.ecsite.model.mapper.TblPurchaseMapper;

@Controller
@RequestMapping("/ecsite")
public class indexController {
	
	//①MstGoodsを介してmst_goodsテーブルにアクセスするためのmapper(DAO) 画面遷移
	@Autowired//←自動的にインスタンス化
	public MstGoodsMapper goodsMapper;
	
	//④MstUserを介してmst_userテーブルにアクセスするためのmapper(DAO)　ログイン機能追加
	@Autowired
	public MstUserMapper userMapper;
	//⑤
	private Gson gson = new Gson();
	
	//⑦
	@Autowired
	private TblPurchaseMapper purchaseMapper;
	
	//②
	@GetMapping("/")
	public String index(Model model) {
		List<MstGoods> goods = goodsMapper.findAll();
		model.addAttribute("goods", goods);//modelにデータが入る→それをthyleafに渡す→thyleafがhtmlに翻訳して渡す→画面に表示される
		
		return "index";
	}
	
	//⑥
	@ResponseBody //←文字列そのものが返却される
	@PostMapping("/api/login")
	public String loginApi(@RequestBody LoginForm f) {
		MstUser user = userMapper.findByUserNameAndPassword(f); //mst_userテーブルからユーザー名とパスワードで検索し、結果を取得
		
		if(user == null) {
			user = new MstUser();
			user.setFullName("ゲスト");
		}
		
		return gson.toJson(user);
	}
	
	//⑧
	@ResponseBody
	@PostMapping("/api/purchase")
	public int purchaseApi(@RequestBody CartForm f) {
		//↓for文の一種
		f.getCartList().forEach((c) -> {
			int total = c.getPrice() * c.getCount();
			purchaseMapper.insert(f.getUserId(),c.getId(),c.getGoodsName(),c.getCount(), total);
		});
		
		//for(Cart c: form.getCartList()) {
//		purchaseMapper.insert(form.getUserId(), c.getId(), c.getGoodsName(), c.getCount(), c.getPrice() * c.getCount());
//		}←上の代わりにこれでも問題ない
		
		return f.getCartList().size();
	}
	
	@ResponseBody
	@PostMapping("/api/history")
	public String historyApi(@RequestBody HistoryForm f) {
		int userId = f.getUserId();
		List<HistoryDto> history = purchaseMapper.findHistory(userId);
		
		return gson.toJson(history);
	}

}
