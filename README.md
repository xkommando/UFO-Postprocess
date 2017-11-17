# UFO-Postprocess
Decode, process UFO events and construct constraints to detect UaF


1. Uncompress traces of a process 
java -jar UFOUnzip.jar  $input_dir $output_dir

example:

java -jar UFOUnzip.jar ~/benchmarks/chromium2/ufo_traces_27171  unzipped27171

if there is IO exception, the trace file for one thread is broken, but the traces for the rest of the thread are still useful.

2. config trace analyzer.

	there are two ways to config:
		1. use command line, same as RVPredict
		2. use file "config.properties", "solver_time", "trace_dir", "solver_mem", "window_size" can be configured here. The config.properties will override command line arguments.

3. run trace analyzer, main class file: Main.java
