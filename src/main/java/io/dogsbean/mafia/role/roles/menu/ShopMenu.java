package io.dogsbean.mafia.role.roles.menu;

import io.dogsbean.mafia.game.player.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class ShopMenu implements Listener {

    private final ItemStack[] shopItems = {
            createShopItem(Material.SHEARS, "가위", 3000),
            createShopItem(Material.DIAMOND_SWORD, "칼", 13000),
            createShopItem(Material.STRING, "끈", 4500),
            createShopItem(Material.FLINT_AND_STEEL, "라이터", 600)
    };

    public void openShopMenu(Player player) {
        Inventory shopInventory = Bukkit.createInventory(null, 9, "상점 메뉴");

        for (ItemStack item : shopItems) {
            shopInventory.addItem(item);
        }

        player.openInventory(shopInventory);
    }

    private static ItemStack createShopItem(Material material, String name, int price) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Collections.singletonList("가격: " + price + "원"));
        item.setItemMeta(itemMeta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("상점 메뉴")) {
            event.setCancelled(true); // 인벤토리 클릭 방지

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
                Player player = (Player) event.getWhoClicked();
                PlayerProfile profile = PlayerProfile.getProfile(player);

                // 가격 확인
                int price = getItemPrice(clickedItem.getType());
                if (profile.getMoney() >= price) {
                    profile.removeMoney(price); // 돈 차감
                    player.getInventory().addItem(clickedItem.clone()); // 아이템 지급
                    player.sendMessage(clickedItem.getItemMeta().getDisplayName() + "을(를) 구매했습니다.");
                } else {
                    player.sendMessage("돈이 부족합니다! 필요한 금액: " + price + "원.");
                }
            }
        }
    }

    private int getItemPrice(Material material) {
        switch (material) {
            case SHEARS:
                return 5000;
            case DIAMOND_SWORD:
                return 10000;
            case STRING:
                return 1000;
            case WATER_BUCKET:
                return 2000;
            default:
                return 0;
        }
    }
}