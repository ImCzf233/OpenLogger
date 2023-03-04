package com.netease.mc.modSS.managers;

import java.util.*;
import net.minecraft.util.*;
import net.minecraft.client.*;
import java.net.*;
import com.netease.mc.modSS.protecter.debugger.*;
import javax.imageio.*;
import net.minecraft.client.renderer.texture.*;
import java.io.*;

public class CapeManager
{
    private HashMap<String, ResourceLocation> CapeUsers;
    private HashMap<String, ResourceLocation> Capes;
    
    public CapeManager() {
        this.CapeUsers = new HashMap<String, ResourceLocation>();
        this.Capes = new HashMap<String, ResourceLocation>();
        if (Minecraft.getMinecraft().thePlayer != null) {
            this.LoadCapes();
        }
    }
    
    public void LoadCapes() {
        try {
            URL l_URL = null;
            URLConnection l_Connection = null;
            BufferedReader l_Reader = null;
            l_URL = new URL("http://115.126.43.76/cape/cape.txt");
            l_Connection = l_URL.openConnection();
            l_Connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
            l_Reader = new BufferedReader(new InputStreamReader(l_Connection.getInputStream()));
            String l_Line;
            while ((l_Line = l_Reader.readLine()) != null) {
                final String[] l_Split = l_Line.split(" ");
                if (l_Split.length == 2) {
                    this.DownloadCapeFromLocationWithName(l_Split[0], l_Split[1]);
                }
            }
            l_Reader.close();
            l_URL = new URL("http://115.126.43.76/cape/capes.txt");
            l_Connection = l_URL.openConnection();
            l_Connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
            l_Reader = new BufferedReader(new InputStreamReader(l_Connection.getInputStream()));
            while ((l_Line = l_Reader.readLine()) != null) {
                this.ProcessCapeString(l_Line);
            }
            l_Reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void reload() {
        try {
            URL l_URL = null;
            URLConnection l_Connection = null;
            BufferedReader l_Reader = null;
            l_URL = new URL("http://115.126.43.76/cape/cape.txt");
            l_Connection = l_URL.openConnection();
            l_Connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
            l_Reader = new BufferedReader(new InputStreamReader(l_Connection.getInputStream()));
            String l_Line;
            while ((l_Line = l_Reader.readLine()) != null) {
                final String[] l_Split = l_Line.split(" ");
                if (l_Split.length == 2) {
                    this.DownloadCapeFromLocationWithName(l_Split[0], l_Split[1]);
                }
            }
            l_Reader.close();
            l_URL = new URL("http://115.126.43.76/cape/capes.txt");
            l_Connection = l_URL.openConnection();
            l_Connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
            l_Reader = new BufferedReader(new InputStreamReader(l_Connection.getInputStream()));
            while ((l_Line = l_Reader.readLine()) != null) {
                this.ProcessCapeString(l_Line);
            }
            l_Reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void ProcessCapeString(final String p_String) {
        final String[] l_Split = p_String.split(" ");
        if (l_Split.length == 2) {
            final ResourceLocation l_Cape = this.GetCapeFromName(l_Split[1]);
            if (l_Cape != null) {
                this.CapeUsers.put(ShellNative.getSubString(l_Split[0], "{", "}"), l_Cape);
            }
        }
    }
    
    private final ResourceLocation GetCapeFromName(final String p_Name) {
        if (!this.Capes.containsKey(p_Name)) {
            return null;
        }
        return this.Capes.get(p_Name);
    }
    
    public void DownloadCapeFromLocationWithName(final String p_Link, final String p_Name) throws IOException {
        final DynamicTexture l_Texture = new DynamicTexture(ImageIO.read(new URL(p_Link)));
        this.Capes.put(p_Name, Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("modlogo", l_Texture));
    }
    
    public ResourceLocation getCapeByName(final String name) {
        if (!this.CapeUsers.containsKey(name)) {
            return null;
        }
        return this.CapeUsers.get(name);
    }
}
