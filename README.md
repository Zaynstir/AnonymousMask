# AnonymousMask
This plugin is fairly simple. You type /mask to recieve a mask. Then you right click the mask and it will hide your nametag until you take off the mask.
# A couple of things to take note of how it currently works.
- This plugin works by adding you to a team called 'hidden' when you put on the mask and removes you from it when you take it off.
- The Plugin will save your previous team when you put on the mask, and then it will return you back to your team when you take off the mask.
- This occurs even when your reload the plugin or restart the server
- It accomplishes this by saving all players in the "hidden" team to a JSON file in onDisable(), and then reads that file in onEnable()
- If you change your team while wearing the mask, your name will appear.
