package com.samujjwaal.hw2.util;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;


/**
 * Mahout's XMLInputFormat with support for multiple input and output tags referred from
 * https://github.com/Mohammed-siddiq/hadoop-XMLInputFormatWithMultipleTags
 *
 * An implementation of XML input format.
 * Supports multiple start and end tags sent from the configuration.
 * start tags and end tags are delimited by ,(comma)
 */
public class XmlInputFormatWithMultipleTags extends TextInputFormat {

    public static final String START_TAG_KEYS = "xmlinput.start";
    public static final String END_TAG_KEYS = "xmlinput.end";


    @Override
    public RecordReader<LongWritable, Text> createRecordReader(InputSplit is, TaskAttemptContext tac) {

        return new XmlRecordReader();

    }

    public static class XmlRecordReader extends RecordReader<LongWritable, Text> {
        private byte[][] startTags;
        private byte[][] endTags;
        private long start;
        private long end;
        private FSDataInputStream fsin;
        private DataOutputBuffer buffer = new DataOutputBuffer();
        private LongWritable key = new LongWritable();
        private Text value = new Text();


        @Override
        public void initialize(InputSplit is, TaskAttemptContext tac) throws IOException, InterruptedException {
            FileSplit fileSplit = (FileSplit) is;

            String[] sTags = tac.getConfiguration().get(START_TAG_KEYS).split(",");
            String[] eTags = tac.getConfiguration().get(END_TAG_KEYS).split(",");

            startTags = new byte[sTags.length][];
            endTags = new byte[sTags.length][];

            IntStream.range(0, sTags.length)
                    .forEach(i -> {
                        startTags[i] = sTags[i].getBytes(StandardCharsets.UTF_8);
                        endTags[i] = eTags[i].getBytes(StandardCharsets.UTF_8);
                    });

            start = fileSplit.getStart();
            end = start + fileSplit.getLength();
            Path file = fileSplit.getPath();

            FileSystem fs = file.getFileSystem(tac.getConfiguration());
            fsin = fs.open(fileSplit.getPath());
            fsin.seek(start);
        }

        @Override
        public boolean nextKeyValue() throws IOException, InterruptedException {
            if (fsin.getPos() < end) {
                //perform readuntillmatch for any of the specified tag
                int res = readUntilMatch(startTags, false);
                if (res != -1) { // Read until start_tag1 or start_tag2
                    try {
                        buffer.write(startTags[res - 1]);
                        //read all the contents before the end tag
                        int res1 = readUntilMatch(endTags, true);
                        if (res1 != -1) {
                            // updating the buffer with contents between start and end tags.
                            value.set(buffer.getData(), 0, buffer.getLength());
                            key.set(fsin.getPos());
                            return true;
                        }
                    } finally {
                        buffer.reset();
                    }
                }
            }
            return false;
        }

        @Override
        public LongWritable getCurrentKey() throws IOException, InterruptedException {
            return key;
        }

        @Override
        public Text getCurrentValue() throws IOException, InterruptedException {
            return value;


        }

        @Override
        public float getProgress() throws IOException, InterruptedException {
            return (fsin.getPos() - start) / (float) (end - start);
        }

        @Override
        public void close() throws IOException {
            fsin.close();
        }

        private int readUntilMatch(byte[][] match, boolean withinBlock) throws IOException {
            int[] tagPointers = new int[match.length];

            while (true) {
                int currentByte = fsin.read();
                // end of file:
                if (currentByte == -1) return -1;
                // save to buffer:
                if (withinBlock) buffer.write(currentByte);

                // check if we're matching any of the tag specified:

                for (int i = 0; i < tagPointers.length; i++) {
                    if (currentByte == match[i][tagPointers[i]]) {
                        tagPointers[i]++;
                        if (tagPointers[i] >= match[i].length)
                            return i + 1;
                    } else
                        tagPointers[i] = 0;
                }
                // see if we've passed the stop point:
                if (!withinBlock && checkAllPointers(tagPointers) && fsin.getPos() >= end)
                    return -1;
            }
        }

        private boolean checkAllPointers(int[] tagPointers) {
            for (int i : tagPointers) {
                if (i != 0)
                    return false;
            }
            return true;
        }
    }
}
