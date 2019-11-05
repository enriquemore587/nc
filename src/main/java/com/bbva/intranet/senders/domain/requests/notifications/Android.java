package com.bbva.intranet.senders.domain.requests.notifications;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Android {

    private String body; // (string, optional): The notification's body text.,
    private String sound; // (string, optional): The sound to play when the device receives the notification. Supports "default" or the filename of a sound resource bundled in the app. Sound files must reside in /res/raw/.,
    private String title; // (string, optional): The notification's title.,
    private String color; // (string, optional): The notification's icon color, expressed in "#rrggbb" format.,
    @SerializedName("title_loc_args")
    private List<String> titleLocArgs; // (string, optional): Variable string values to be used in place of the format specifiers in title_loc_key to use to localize the title text to the user's current localization.,
    private String image; // (string, optional): Contains the URL of an image that is going to be displayed in a notification. If present, it will override google.firebase.fcm.v1.Notification.image.,
    @SerializedName("body_loc_args")
    private List<String> bodyLocArgs; // (string, optional): Variable string values to be used in place of the format specifiers in body_loc_key to use to localize the body text to the user's current localization.,
    @SerializedName("title_loc_key")
    private String titleLocKey; // (string, optional): The key to the title string in the app's string resources to use to localize the title text to the user's current localization.,
    @SerializedName("channel_id")
    private String channelId; // (string, optional): The app must create a channel with this channel ID before any notification with this channel ID is received. If you don't singleSend this channel ID in the request, or if the channel ID provided has not yet been created by the app, FCM uses the channel ID specified in the app manifest.,
    private String tag; // (string, optional): Identifier used to replace existing notifications in the notification drawer. If not specified, each request creates a new notification. If specified and a notification with the same tag is already being shown, the new notification replaces the existing one in the notification drawer.,
    @SerializedName("click_action")
    private String clickAction; // (string, optional): The action associated with a user click on the notification. If specified, an activity with a matching intent filter is launched when a user clicks on the notification.,
    private String icon; // (string, optional): The notification's icon. Sets the notification icon to myicon for drawable resource myicon. If you do not singleSend this key in the request, FCM displays the launcher icon specified in your app manifest.,
    @SerializedName("body_loc_key")
    private String bodyLocKey; // (string, optional): The key to the body string in the app's string resources to use to localize the body text to the user's current localization.

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getTitleLocArgs() {
        return titleLocArgs;
    }

    public void setTitleLocArgs(List<String> titleLocArgs) {
        this.titleLocArgs = titleLocArgs;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getBodyLocArgs() {
        return bodyLocArgs;
    }

    public void setBodyLocArgs(List<String> bodyLocArgs) {
        this.bodyLocArgs = bodyLocArgs;
    }

    public String getTitleLocKey() {
        return titleLocKey;
    }

    public void setTitleLocKey(String titleLocKey) {
        this.titleLocKey = titleLocKey;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getClickAction() {
        return clickAction;
    }

    public void setClickAction(String clickAction) {
        this.clickAction = clickAction;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBodyLocKey() {
        return bodyLocKey;
    }

    public void setBodyLocKey(String bodyLocKey) {
        this.bodyLocKey = bodyLocKey;
    }

    @Override
    public String toString() {
        return "Android{" +
                "body='" + body + '\'' +
                ", sound='" + sound + '\'' +
                ", title='" + title + '\'' +
                ", color='" + color + '\'' +
                ", titleLocArgs=" + titleLocArgs +
                ", image='" + image + '\'' +
                ", bodyLocArgs=" + bodyLocArgs +
                ", titleLocKey='" + titleLocKey + '\'' +
                ", channelId='" + channelId + '\'' +
                ", tag='" + tag + '\'' +
                ", clickAction='" + clickAction + '\'' +
                ", icon='" + icon + '\'' +
                ", bodyLocKey='" + bodyLocKey + '\'' +
                '}';
    }
}
