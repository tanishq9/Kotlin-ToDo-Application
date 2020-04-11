package com.boss.login.clickListeners

import com.boss.login.db.Notes

interface ItemClickListener {
    fun onClick(note: Notes)
    fun onUpdate(note: Notes)
}