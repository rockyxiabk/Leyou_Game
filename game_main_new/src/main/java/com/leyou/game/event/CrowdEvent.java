package com.leyou.game.event;

/**
 * Description : com.leyou.game.event
 *
 * @author : rocky
 * @Create Time : 2017/8/31 下午8:13
 * @Modified Time : 2017/8/31 下午8:13
 */
public class CrowdEvent {
    private boolean deleteResult;
    private boolean inviteResult;
    private int deleteCrowdMember;

    public CrowdEvent(boolean deleteResult) {
        this.deleteResult = deleteResult;
    }

    public CrowdEvent(int deleteCrowdMember) {
        this.deleteCrowdMember = deleteCrowdMember;
    }

    public CrowdEvent(boolean deleteResult, boolean inviteResult) {
        this.deleteResult = deleteResult;
        this.inviteResult = inviteResult;
    }

    public boolean isDeleteResult() {
        return deleteResult;
    }

    public void setDeleteResult(boolean deleteResult) {
        this.deleteResult = deleteResult;
    }

    public boolean isInviteResult() {
        return inviteResult;
    }

    public void setInviteResult(boolean inviteResult) {
        this.inviteResult = inviteResult;
    }

    public int getDeleteCrowdMember() {
        return deleteCrowdMember;
    }

    public void setDeleteCrowdMember(int deleteCrowdMember) {
        this.deleteCrowdMember = deleteCrowdMember;
    }
}
