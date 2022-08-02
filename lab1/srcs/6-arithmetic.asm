	.data
a:
	10
d:
	-3
n:
	6
	.text
main:
	load %x0, $a, %x3	@load a into reg3
	load %x0, $d, %x4	@load d into reg4
	load %x0, $n, %x5	@load n into reg5
	add %x0, %x0, %x6	@use reg6 for increasing d
	addi %x0, %x0, %x8
	sub %x0, %x5, %x9	@load -n in reg9 to end program
loop:
	add %x6, %x3, %x7	@the terms of the AP in reg7
	add %x6, %x4, %x6	@update common difference
	store %x7, 65535, %x8	@store the term
	subi %x8, 1, %x8	@give a negative offset
	beq %x8, %x9, endl	@end program
	jmp loop		@jump back into the loop
endl:
	end
