package org.vivi.framework.report.service.common.enums;


import org.vivi.framework.report.service.common.enums.base.BaseCharEnum;

/**
 * @ClassName: RequestHeaderEnums
 * @Description: 请求头用枚举类
*/
public enum RequestHeaderEnums implements BaseCharEnum {

	Authorization {
		public String getCode() {
			return "Authorization";
		}

		public String getName() {
			return "授权";
		}
	},
	ReqSource {
		public String getCode() {
			return "reqSource";
		}

		public String getName() {
			return "请求类型";
		}
	},
	Version {
		public String getCode() {
			return "version";
		}

		public String getName() {
			return "版本号";
		}
	},
	DeviceId {
		public String getCode() {
			return "deviceId";
		}

		public String getName() {
			return "设备标识";
		}
	},
	DeviceType{
		public String getCode() {
			return "deviceType";
		}

		public String getName() {
			return "设备类型";
		}
	},
	
}
