package me.unleqitq.customtickspeed;

import java.util.HashMap;
import java.util.Map;

public enum Version {
	SERVER_1_14_4("1.14.4", "3dc3d84a581f14691199cf6831b71ed1296a9fdf", "46a7ba37c15820f00d49eafb38afb4a9bb64a0be"),
	SERVER_1_15("1.15", "e9f105b3c5c7e85c7b445249a93362a22f62442d", "c79e5ee9c5167b730266910d4c5bafbaf27c2f52"),
	SERVER_1_15_1("1.15.1", "4d1826eebac84847c71a77f9349cc22afd0cf0a1", "d10e23f8def30fcf7d0a0d027f48f2731d80208f"),
	SERVER_1_15_2("1.15.2", "bb2b6b1aefcd70dfd1892149ac3a215f6c636b07", "e018f7413ad5b98d7427bc3027c95c78845e891b"),
	SERVER_1_16("1.16", "a0d03225615ba897619220e256a266cb33a44b6b", "a11471890ef5bdc4025dd7a587a46f106d56a7da"),
	SERVER_1_16_1("1.16.1", "a412fd69db1f81db3f511c1463fd304675244077", "a11471890ef5bdc4025dd7a587a46f106d56a7da"),
	SERVER_1_16_2("1.16.2", "c5f6fb23c3876461d46ec380421e42b289789530", "0dbbb5aae568c2d9aa34e3be11e7b525054265d9"),
	SERVER_1_16_3("1.16.3", "f02f4473dbf152c23d7d484952121db0b36698cb", "e75ff1e729aec4a3ec6a94fe1ddd2f5a87a2fd00"),
	SERVER_1_16_4("1.16.4", "35139deedbd5182953cf1caa23835da59ca3d7cd", "d9ae0e8e28475254855430ff051daaa0dd041a08"),
	SERVER_1_16_5("1.16.5", "1b557e7b033b583cd9f66746b7a9ab1ec1673ced", "41285beda6d251d190f2bf33beadd4fee187df7a"),
	SERVER_1_17("1.17", "0a269b5f2c5b93b1712d0f5dc43b6182b9ab254e", "84d80036e14bc5c7894a4fad9dd9f367d3000334"),
	SERVER_1_17_1("1.17.1", "a16d67e5807f57fc4e550299cf20226194497dc2", "f6cae1c5c1255f68ba4834b16a0da6a09621fe13"),
	SERVER_1_18("1.18", "3cf24a8694aca6267883b17d934efacc5e44440d", "a8fe854e35a69df7289d3f03fc0821f6363f2238"),
	SERVER_1_18_1("1.18.1", "125e5adf40c659fd3bce3e66e67a16bb49ecc1b9", "9717df2acd926bd4a9a7b2ce5f981bb7e4f7f04a"),
	SERVER_1_18_2("1.18.2", "c8f83c5655308435b3dcf03c06d9fe8740a77469", "e562f588fea155d96291267465dc3323bfe1551b"),
	SERVER_1_19("1.19", "e00c4052dac1d59a1188b2aa9d5a87113aaf1122", "1c1cea17d5cd63d68356df2ef31e724dd09f8c26"),
	SERVER_1_19_1("1.19.1", "8399e1211e95faa421c1507b322dbeae86d604df", "3565648cdd47ae15738fb804a95a659137d7cfd3"),
	SERVER_1_19_2("1.19.2", "f69c284232d7c7580bd89a5a4931c3581eae1378", "ed5e6e8334ad67f5af0150beed0f3d156d74bd57"),
	SERVER_1_19_3("1.19.3", "c9df48efed58511cdd0213c56b9013a7b5c9ac1f", "bc44f6dd84cd2f3ad8c0caad850eaca9e82067e3"),
	;
	
	
	private final String version;
	
	private final String jar;
	
	private final String mappings;
	
	private static final String OBJECTS = "https://launcher.mojang.com/v1/objects/";
	
	private static final Map<String, Version> SERVER_VERSION_MAP;
	
	
	Version(String version, String jar, String mappings) {
		this.version = version;
		this.jar = jar + "/server.jar";
		this.mappings = mappings + "/server.txt";
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public String getJar() {
		return "https://launcher.mojang.com/v1/objects/" + this.jar;
	}
	
	public String getMappings() {
		return "https://launcher.mojang.com/v1/objects/" + this.mappings;
	}
	
	public String toString() {
		return "Version{version='" + this.version + "'}";
	}
	
	static {
		SERVER_VERSION_MAP = new HashMap<>();
		for (Version ver : values()) {
			SERVER_VERSION_MAP.put(ver.getVersion(), ver);
		}
	}
	
	public static Version getByVersion(String ver) {
		return SERVER_VERSION_MAP.get(ver);
	}
}
