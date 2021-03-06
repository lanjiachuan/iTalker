package com.example.ggxiaozhi.factory;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.ggxiaozhi.common.Common;
import com.example.ggxiaozhi.common.app.Application;
import com.example.ggxiaozhi.factory.data.DataSource;
import com.example.ggxiaozhi.factory.data.group.GroupCenter;
import com.example.ggxiaozhi.factory.data.group.GroupDispatcher;
import com.example.ggxiaozhi.factory.data.helper.UserHelper;
import com.example.ggxiaozhi.factory.data.message.MessageCenter;
import com.example.ggxiaozhi.factory.data.message.MessageDispatcher;
import com.example.ggxiaozhi.factory.data.user.UserCenter;
import com.example.ggxiaozhi.factory.data.user.UserDispatcher;
import com.example.ggxiaozhi.factory.model.RspModel;
import com.example.ggxiaozhi.factory.model.api.PushModel;
import com.example.ggxiaozhi.factory.model.card.GroupCard;
import com.example.ggxiaozhi.factory.model.card.GroupMemberCard;
import com.example.ggxiaozhi.factory.model.card.MessageCard;
import com.example.ggxiaozhi.factory.model.card.UserCard;
import com.example.ggxiaozhi.factory.model.db.User;
import com.example.ggxiaozhi.factory.presistance.Account;
import com.example.ggxiaozhi.factory.utils.DBFlowExclusionStrategies;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 工程名 ： ITalker
 * 包名   ： com.example.ggxiaozhi.factory
 * 作者名 ： 志先生_
 * 日期   ： 2017/11
 * 功能   ：提供上传逻辑需要的参数
 */

public class Factory {
    private static final String TAG = "Factory";
    //单例模式
    private static final Factory instance;
    //全局的线程池
    private final Executor mExecutor;
    private final Gson mGson;

    static {
        instance = new Factory();
    }

    private Factory() {
        //创建一个4个线程的线程池
        mExecutor = Executors.newFixedThreadPool(4);
        mGson = new GsonBuilder()
                //设置时间格式
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                // 设置一个过滤器，数据库级别的Model(BaseModel)不进行Json转换
                .setExclusionStrategies(new DBFlowExclusionStrategies())
                .create();

    }

    /**
     * Factory 中的初始化
     * 主要是SP 与DBflow的初始化
     */
    public static void setup() {

        //DBflow的初始化
        FlowManager.init(new FlowConfig.Builder(app())
                .openDatabasesOnInit(true)//数据库初始化的时候就打开数据库
                .build());

        // 持久化的数据进行初始化SP
        Account.load(Factory.app());
    }

    public static Application app() {
        return Application.getInstance();
    }


    /**
     * 异步执行的方法
     *
     * @param runnable
     */
    public static void runOnAsync(Runnable runnable) {
        //拿到单例 拿到线程池 然后异步执行
        instance.mExecutor.execute(runnable);
    }

    /**
     * 返回一个全局的Gson 在这里可以进行Gson的一些全局的初始化
     *
     * @return
     */
    public static Gson getGson() {
        return instance.mGson;
    }

    /**
     * 进行错误Code的解析，
     * 把网络返回的Code值进行统一的规划并返回为一个String资源
     *
     * @param model    RspModel
     * @param callback DataSource.FailedCallback 用于返回一个错误的资源Id
     */
    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback) {
        if (model == null)
            return;

        // 进行Code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
//                Application.showToast(R.string.data_rsp_error_account_token);
                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    private static void decodeRspCode(@StringRes final int resId,
                                      final DataSource.FailedCallback callback) {
        if (callback != null)
            callback.onDataNotAvailable(resId);
    }

    /**
     * 收到账户退出的消息需要进行账户退出重新登录
     */
    private void logout() {
        Intent intent = new Intent("com.example.broadcastbestpracrice.FORCE_OFFLINE");
        Factory.app().sendBroadcast(intent);

    }

    /**
     * 处理推送来的消息
     */
    public static void dispatchMessage(String message, Context context, PendingIntent pendingIntent) {
        //首次检查是否是登录状态
        if (!Account.isLogin())
            return;
        PushModel pushModel = PushModel.decode(message);
        if (pushModel == null)
            return;
        // 对推送集合进行遍历
        for (PushModel.Entity entity : pushModel.getEntities()) {
            Log.d(TAG, "dispatchMessage-Entity: " + pushModel.getEntities().toString());

            switch (entity.type) {
                case PushModel.ENTITY_TYPE_LOGOUT:
                    instance.logout();
                    // 退出情况下，直接返回，并且不可继续
                    break;
                case PushModel.ENTITY_TYPE_MESSAGE: {
                    // 普通消息
                    MessageCard card = getGson().fromJson(entity.content, MessageCard.class);
                    getMessageCenter().dispatch(card);
                    if (!getCurrentTask(context)) {
                        createNotification(card, context, pendingIntent);
                    }
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_FRIEND: {
                    // 好友添加
                    UserCard card = getGson().fromJson(entity.content, UserCard.class);
                    getUserCenter().dispatch(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_GROUP: {
                    // 添加群
                    GroupCard card = getGson().fromJson(entity.content, GroupCard.class);
                    getGroupCenter().groupDispatch(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS:
                case PushModel.ENTITY_TYPE_MODIFY_GROUP_MEMBERS: {
                    // 群成员变更, 回来的是一个群成员的列表
                    Type type = new TypeToken<List<GroupMemberCard>>() {
                    }.getType();
                    List<GroupMemberCard> card = getGson().fromJson(entity.content, type);
                    // 把数据集合丢到数据中心处理
                    getGroupCenter().groupMemberDispatch(card.toArray(new GroupMemberCard[0]));
                    break;
                }
                case PushModel.ENTITY_TYPE_EXIT_GROUP_MEMBERS: {
                    // TODO 成员退出的推送
                }
            }
        }
    }

    /**
     * 创建一个消息通知
     *
     * @param context
     */
    private static void createNotification(MessageCard card, Context context, PendingIntent pendingIntent) {
        //获取发送者
        SharedPreferences preferences = context.getSharedPreferences("notificationinfo", Context.MODE_PRIVATE);
        boolean isSound = preferences.getBoolean(Common.Constance.NOTI_KEY_IS_SOUND, true);
        boolean isVibrate = preferences.getBoolean(Common.Constance.NOTI_KEY_IS_VIBRATE, true);
        boolean isLight = preferences.getBoolean(Common.Constance.NOTI_KEY_IS_LIGTHS, true);
        int light = isLight ? 1000 : 0;
        User user = UserHelper.findFromLocal(card.getSenderId());
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(user.getName())
                .setContentText(card.getContent())
                .setWhen(card.getCreateAt().getTime())
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setContentIntent(pendingIntent)
                .setSound(isSound ? Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.fu) : Uri.parse(""))
                .setVibrate(isVibrate ? new long[]{0, 1000, 1000, 1000} : new long[]{0, 0, 0, 0})
                .setLights(Color.WHITE, light, light)
                .build();
        manager.notify(1, notification);
    }

    /**
     * 这个是真正的获取指定包名的应用程序是否在运行(无论前台还是后台)
     *
     * @return true已启动
     */
    public static boolean getCurrentTask(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> appProcessInfos = activityManager.getRunningTasks(50);
        for (ActivityManager.RunningTaskInfo process : appProcessInfos) {

            if (process.baseActivity.getPackageName().equals(context.getPackageName())
                    || process.topActivity.getPackageName().equals(context.getPackageName())) {

                return true;
            }
        }
        return false;
    }

    /**
     * 用户信息管理中心的入口
     *
     * @return 实例
     */
    public static UserCenter getUserCenter() {
        return UserDispatcher.instance();
    }

    /**
     * 消息信息管理中心的入口
     *
     * @return 实例
     */
    public static MessageCenter getMessageCenter() {
        return MessageDispatcher.instance();
    }

    /**
     * 群信息管理中心的入口
     *
     * @return 实例
     */
    public static GroupCenter getGroupCenter() {
        return GroupDispatcher.instance();
    }
}
