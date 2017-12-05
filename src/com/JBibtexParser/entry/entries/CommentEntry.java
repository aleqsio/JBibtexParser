package com.JBibtexParser.entry.entries;

import com.JBibtexParser.entry.IEntry;

/**
 * An entry repesenting a bibex comment - either a part of file not surrounded by @*{}, or a @comment{} entry
 */
public class CommentEntry implements IEntry {

	private String commentText;

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	@Override
	public String toString() {
		return "comment: " +commentText.substring(0,Math.min(commentText.length(),20)) + (commentText.length()>20?" ...":"") ;
	}

	@Override
	public String getName() {
		return toString();
	}
}