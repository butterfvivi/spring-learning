package org.vivi.framework.lucky.simple.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * To change this template use File | Settings | File Templates.
 */
@Data
public class BaseOperationModel implements BaseModel,Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
	private Date create_time;
    /**
     * 修改时间
     */
    private Date update_time;
    /**
     * 创建人id
     */
    private Long create_user_id;
    /**
     * 修改人id
     */
    private Long update_user_id;


}
