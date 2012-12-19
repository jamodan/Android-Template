/*
 * Created By : Daniel Jamison
 * Copyright (c) 2012 South Dakota State University. All rights reserved.
 */
package com.example.template;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
 
public class SettingsDisclaimerTab extends Fragment {
 
    public static Fragment newInstance(Context context) {
    	SettingsDisclaimerTab f = new SettingsDisclaimerTab();
 
        return f;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.settings_disclaimer_tab, null);
        //View root = inflater.inflate(R.layout.disclaimer, container, false);
        
        TextView disclaimer = (TextView)root.findViewById(R.id.disclaimer);
        
        String scheme = "";
        
        MatchFilter newlineFilter = new MatchFilter() {
            public boolean acceptMatch(CharSequence s, int start, int end) {
            	return (start == 0 || s.charAt(start-1) != '\n' );
            }
        };

        Pattern igrowPattern = Pattern.compile("iGrow");
        TransformFilter igrowFilter = new TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return new String("http://www.iGrow.org/");
            }
        };

        Pattern extensionPattern = Pattern.compile("SDSU Extension");
        TransformFilter extensionFilter = new TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return new String("http://sdstate.edu/sdsuextension/");
            }
        };
    	
        Linkify.addLinks(disclaimer, Linkify.EMAIL_ADDRESSES);
	    Linkify.addLinks(disclaimer, igrowPattern, scheme, newlineFilter, igrowFilter);
	    Linkify.addLinks(disclaimer, extensionPattern, scheme, newlineFilter, extensionFilter);
	    
        return root;
    }
 
}