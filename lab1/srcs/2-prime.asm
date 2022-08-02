	.data
a:
	10
	.text
main:
	load %x0, $a, %x3	@load a into reg3
	addi %x0, 2, %x4	@load 2 into reg4       
	bgt %x4, %x3, fail	@takes care of all inputs < 2
loop:
	mul %x4, %x4, %x5	@check all natural numbers <= sqrt(n)
	div %x3, %x4, %x6
	beq %x31, %x0, fail	@if divisible, then not prime
	addi %x4, 1, %x4
	bgt %x5, %x3, success	@if not divisible by any number, the prime
	jmp loop
fail:
	subi %x0, 1, %x10
	end
success:
	addi %x0, 1, %x10
	end
