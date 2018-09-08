package yyl.mvc.plug.security;

import java.io.Serializable;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 用户登录对象
 * @author _yyl
 */
@SuppressWarnings("serial")
public class Principal implements Serializable {

    // ==============================Fields========================================
    /** 用户ID */
    private Long userId;
    /** 用户账号 */
    private String account;
    /** 用户姓名 */
    private String name;
    /** 所属机构 */
    private Long orgId;
    /** 所属角色 */
    private Long[] roleIds = new Long[0];
    /** 拥有权限 */
    private Long[] permissionIds = new Long[0];

    // ==============================Constants=====================================
    /** 未登录用户 */
    public static final Principal NONE;
    static {
        NONE = new Principal();
        NONE.setUserId(-1L);
        NONE.setAccount("#");
        NONE.setName("#");
        NONE.setOrgId(-1L);
        NONE.setRoleIds(ArrayUtils.EMPTY_LONG_OBJECT_ARRAY);
        NONE.setPermissionIds(ArrayUtils.EMPTY_LONG_OBJECT_ARRAY);
    }

    // ==============================PropertyAccessors==============================
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public Long[] getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(Long[] permissionIds) {
        this.permissionIds = permissionIds;
    }
}
