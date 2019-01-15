package net.oopscraft.application.core.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import net.oopscraft.application.core.TextTable;

public class LuceneAnalyzer {
	
	public static List<String> skipWords = new ArrayList<String>();
	// TODO
	static {
		skipWords.add("");
	}

	/**
	 * analyze
	 * @param text
	 * @param analyzer
	 * @return
	 * @throws IOException
	 */
	private static List<String> analyze(String text, Analyzer analyzer) throws IOException{
	    List<String> result = new ArrayList<String>();
	    TokenStream tokenStream = analyzer.tokenStream(" ", text);
	    CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
	    tokenStream.reset();
	    while(tokenStream.incrementToken()) {
	    	String word = attr.toString();
	    	if(skipWords.contains(word)) {
	    		continue;
	    	}
	    	result.add(attr.toString());
	    }       
	    return result;
	}

	/**
	 * getTopRank
	 * @param keywords
	 * @param rank
	 * @return
	 * @throws Exception
	 */
	private static List<String> getTopRank(List<String> keywords, int rank) throws Exception {
		Map<String, Integer> map = new HashMap<String,Integer>();
		for(String keyword : keywords) {
			map.put(keyword, (map.get(keyword) == null ? 1 : map.get(keyword) + 1));
		}
		Set<Entry<String, Integer>> set = map.entrySet();
	    List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
	    Collections.sort( list, new Comparator<Map.Entry<String, Integer>>() {
	    	public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 ){
	    		return (o2.getValue()).compareTo( o1.getValue() );//Descending order
	    	}
	    });
	    List<String> topRank = new ArrayList<String>();
	    int i = 0;
	    for(Map.Entry<String, Integer> entry : list){
	    	i ++;
	    	if(i > rank) {
	    		break;
	    	}
	    	topRank.add(entry.getKey());
	    }
	    return topRank;
	}

	/**
	 * standardAnalyze
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static List<String> parseTopKeywords(String text) throws Exception {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		List<String> words = analyze(text, analyzer);
		List<String> rankedKeywords = getTopRank(words,10);
		return rankedKeywords;
	}
	

	
	public static void main(String[] args) throws Exception {
		//String text = "경기 is not 강원 apple";
		String text = "경기 102㎍/㎥였다. 하지만 지난 14일에는 서울·인천·경기·충북·충남·전북·세종 등 무려 7개 시·도에서 100㎍/㎥를 넘겼다. 15일 오전에는 일평균 농도가 100㎍/㎥를 넘긴 곳이 호남과 강원 등으로 확대되기도 했다. 이날 환경부 국립환경과학원은 전국에서 수치가 급격하게 치솟은 이번 고농도 미세먼지에 대해 “굉장히 이례적인 사례”라고 밝혔다. 장임석 국립환경과학원 통합대기질예보센터장은 “추가적인 분석이 필요하지만, 이번에는 잠시 농도가 떨어지는 과정 없이 미세먼지 유입과 대기 정체가 번갈아 이어졌다”면서 “이 과정에서 계단을 오르듯 농도가 치솟은 것으로 보인다”고 설명했다. 고농도 미세먼지는 국내 대기가 정체돼 오염물질이 잘 퍼지지 않는 상황에서 중국발 미세먼지가 흘러들어오면서 심해지는 경향을 보여왔다. 유입된 미세먼지가 국내에서 배출된 오염물질과 결합해 광화학반응을 일으키면 입자가 더 작은 초미세먼지로 악화하는 ‘2차 생성’을 거쳐 수치가 올라가는 것이다. 이번에도 지난 11일쯤부터 기온이 오르면서 국내에서 대기가 정체되고, 중국에서 발생한 미세먼지가 유입되는 과정은 비슷했다. 약한 바람이 문제였을 가능성이 크다. 윤기한 기상청 사무관은 “지난 14일 한반도부터 중국 베이징 주변까지 기압차가 별로 나지 않아 바람이 원활하게 불지 않았다”면서 “한곳에서 오래 머문 오염물질이 서서히 이동해온 것으로 추정해볼 수 있다”고 말했다. 연구자들은 최근 대기 정체의 원인으로 기후변화를 지목하고 있다. 바람을 쌩쌩 불어보내는 상층부의 대기 흐름은 온도차가 결정하는데, 최근 북극 기온이 오르면서 바람도 약해지고 있다는 것이다. 아시아·태평양경제협력체기후센터 이우섭 선임연구원 등은 한국기후변화학회지 최근호에 낸 ‘한반도 미세먼지 발생과 연관된 대기 패턴 그리고 미래 전망’ 논문에서 지구온난화가 지금 추세처럼 계속되면 한반도 주변에서 고농도 미세먼지 사례가 늘어날 것으로 분석했다. 논문에 따르면 고농도 미세먼지가 발생할 때, 한반도 상공에는 고기압이 자리를 잡고 이 고기압이 북극의 찬 공기 유입을 막았다. 상층에서 동서 방향으로 불던 바람이 한반도보다 북쪽을 지나면서, 한반도 지역 풍속은 약화되고 남쪽에서 바람이 불어오기 좋아졌다. 중국에서 오염물질이 흘러와 한반도에 머물기 좋은 조건이 된다는 것이다.연구진은 이러한 메커니즘을 고려해 ‘한국 미세먼지 지표’를 만들고, 이를 지구온난화 시나리오에 대입해 2050~2099년 미세먼지 발생 빈도와 강도를 전망했다. 그 결과 온실가스 감축 노력을 하지 않는 경우에 미세먼지 지표값이 훨씬 크게 예측됐다. 기후변화가 심화되면 겨울철 한반도에서 대기 정체가 잦아지면서 심한 대기오염을 발생시키는 기상 조건이 더 자주 나타난다는 의미로 볼 수 있다. 이우섭 선임연구원은 “온난화가 심해지면 작은 대기오염물질 배출도 고농도 미세먼지로 이어질 수 있다”면서 “오염물질 배출 저감 노력뿐만 아니라 온실가스 배출을 줄이기 위한 노력이 동시에 필요하다”고 밝혔다. ";
		List<String> topKeywords = parseTopKeywords(text);
		System.out.println(topKeywords);
		
	}
	
	
}
