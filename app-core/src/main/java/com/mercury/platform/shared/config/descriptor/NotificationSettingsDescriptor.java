package com.mercury.platform.shared.config.descriptor;

import com.mercury.platform.shared.entity.message.FlowDirections;
import java.awt.event.KeyEvent;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class NotificationSettingsDescriptor implements Serializable {
    private boolean incNotificationEnable = true;
    private boolean outNotificationEnable = true;
    private boolean scannerNotificationEnable = true;
    private boolean whisperHelperEnable = false;
    private HotKeyDescriptor whisperHelperHotKey = new HotKeyDescriptor("F2", 60, false, false, false);
    //private HotKeyDescriptor hideHelperHotKey = new HotKeyDescriptor("F8", 60, false, false, false);
    private int limitCount = 3;
    private int unfoldCount = 2;
    private FlowDirections flowDirections = FlowDirections.DOWNWARDS;
    private boolean dismissAfterKick = true;
    private boolean dismissAfterLeave = false;
    private boolean showLeague;
    private List<ResponseButtonDescriptor> buttons = new ArrayList<>();
    private List<ResponseButtonDescriptor> outButtons = new ArrayList<>();
    private String playerNickname = "Set up your nickname in settings";
    private String accountName = "Set up your accountname in settings";
    private String notificationsInvisibleText = "Hotkey for setting Notifications invisible";
    private String league = "Synthesis";
    private HotKeyDescriptor notificationsInvisibleHotKey = new HotKeyDescriptor("F11", KeyEvent.VK_F11, false, false, false);    
    private List<String> autoCloseTriggers = new ArrayList<>();
}
