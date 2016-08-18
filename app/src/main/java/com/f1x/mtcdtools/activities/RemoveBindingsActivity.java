package com.f1x.mtcdtools.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.f1x.mtcdtools.Messaging;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.KeyInputArrayAdapter;
import com.f1x.mtcdtools.input.KeyInput;

import java.util.Map;

public class RemoveBindingsActivity extends EditBindingsActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_bindings);

        mBindingsListView = (ListView)findViewById(R.id.listViewBindings);
        mKeyInputArrayAdapter = new KeyInputArrayAdapter(this);
        mBindingsListView.setAdapter(mKeyInputArrayAdapter);

        mBindingsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                KeyInput keyInput = mKeyInputArrayAdapter.getItem(position);
                if(keyInput != null) {
                    sendKeyInputEditRequest(Messaging.KeyInputsEditType.REMOVE, keyInput);
                }

                return true;
            }
        });
    }

    @Override
    protected void handleKeyInput(int keyCode) {
        for(int i = 0; i < mKeyInputArrayAdapter.getCount(); ++i) {
            KeyInput keyInput = mKeyInputArrayAdapter.getItem(i);
            View v = mBindingsListView.getChildAt(i);
            if(keyInput.getKeyCode() == keyCode) {
                v.setBackgroundColor(Color.GRAY);
            } else {
                v.setBackgroundColor(Color.WHITE);
            }
        }
    }

    @Override
    public void handleMessage(Message message) {
        switch(message.what) {
            case Messaging.MessageIds.GET_KEY_INPUTS_RESPONSE:
                mKeyInputArrayAdapter.reset((Map<Integer, KeyInput>)message.obj);
                break;
            case Messaging.MessageIds.EDIT_KEY_INPUTS_RESPONSE:
                if(message.arg1 == Messaging.KeyInputsEditResult.FAILURE) {
                    Toast.makeText(this, getString(R.string.KeyBindingRemovalFailure), Toast.LENGTH_LONG).show();
                } else {
                    sendMessage(Messaging.MessageIds.GET_KEY_INPUTS_REQUEST);
                    resetBindingsListViewHighlights();
                }
                break;
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        sendMessage(Messaging.MessageIds.GET_KEY_INPUTS_REQUEST);
    }

    private void resetBindingsListViewHighlights() {
        for(int i = 0; i < mKeyInputArrayAdapter.getCount(); ++i) {
            mBindingsListView.getChildAt(i).setBackgroundColor(Color.WHITE);
        }
    }

    private ListView mBindingsListView;
    private KeyInputArrayAdapter mKeyInputArrayAdapter;
}
